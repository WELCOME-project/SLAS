import os
import logging
import argparse
import rdflib
from rdflib import RDF
from rdflib.namespace import RDFS, SKOS
from lxml import etree


class Taxonomy:

    def __init__(self, path):
        self.graph = rdflib.Graph()
        self.graph.parse(path, format='ttl')

    def generate_xml(self, output_file):

        root = etree.Element('concepts')

        for concept in self.graph.subjects(RDF.type, SKOS["Concept"]):

            concept_comment = self.graph.value(concept, RDFS.comment)       # rdflib.term.Literal('Weakly defended.')
            concept_pref_label = self.graph.value(concept, SKOS.prefLabel)  # rdflib.term.Literal('Soft Target', lang='eng')
            concept_parent = self.graph.value(concept, SKOS.broader)        # rdflib.term.URIRef('http://www.tensor-project.eu/taxomony##7b9cf007-806e-3854-8d12-ab800c8a982b')
            concept_parent_pref_label = self.graph.value(concept_parent, SKOS.prefLabel)

            token = etree.Element('token')
            token.set('label', concept_pref_label)
            token.set('entryId', concept)
            if concept_parent:
                token.set('parentId', concept_parent)
            if concept_parent_pref_label:
                token.set('parentLabel', concept_parent_pref_label)
            if concept_comment:
                token.set('comment', concept_comment)

            variant = etree.Element('variant')
            variant.set('base', concept_pref_label)
            token.append(variant)

            for subject, predicate, obj in self.graph.triples((concept, SKOS.altLabel, None)):
                variant = etree.Element('variant')
                variant.set('base', obj)
                token.append(variant)

            root.append(token)

        with open(output_file, 'wb') as f:
            f.write(etree.tostring(root, pretty_print=True))
        logging.info('XML file containing the taxonomy created in %s' % output_file)


def main():

    logging.basicConfig(level=logging.DEBUG,
                        format="%(asctime)s:%(levelname)s:\t%(message)s")

    parser = argparse.ArgumentParser()

    parser.add_argument("--input_file", dest="input_file", help="File containing the taxonomy; i.e. taxonomy.ttl", required=True)
    parser.add_argument("--output_file", dest="output_file", help="File that will contain the xml; i.e. taxonomy.xml", required=True)

    arguments, unknown = parser.parse_known_args()

    if not os.path.isfile(arguments.input_file):
        logging.error('The selected input file %s does not exist!' % arguments.input_file)
    else:
        logging.info('Creating taxonomy from input file %s ...' % arguments.input_file)
        onto = Taxonomy(arguments.input_file)

        logging.info('generating xml from taxonomy ...')
        onto.generate_xml(arguments.output_file)


if __name__ == "__main__":
    main()
