package edu.upf.taln.welcome.slas.services;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upf.taln.welcome.slas.commons.input.CuniInput;

import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputData;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.DeepAnalysisOutput;

/**
 *
 * @author rcarlini
 */
public class DeepAnalysisServiceTest {
    
    /**
     * Base test to check outputs
     * @param input
     * @param expected
     * @throws java.lang.Exception
     */
    public void testSample(CuniInput input, File expected) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        DeepAnalysisService instance = new DeepAnalysisService();
        
        String expResult = FileUtils.readFileToString(expected, "utf-8");
        DeepAnalysisOutput output = instance.analyze(input);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSampleInitialExtrapolateTurn() throws Exception {

        CuniInput input = new CuniInput();
        input.setModel("en");

        String conll1 = "# sent_id = 1\n" +
            "# text = Hello, can you hear me?\n" +
            "1	Hello	hello	INTJ	UH	_	5	discourse	_	SpaceAfter=No\n" +
            "2	,	,	PUNCT	,	_	5	punct	_	_\n" +
            "3	can	can	AUX	MD	VerbForm=Fin	5	aux	_	_\n" +
            "4	you	you	PRON	PRP	Case=Nom|Person=2|PronType=Prs	5	nsubj	_	_\n" +
            "5	hear	hear	VERB	VB	VerbForm=Inf	0	root	_	_\n" +
            "6	me	I	PRON	PRP	Case=Acc|Number=Sing|Person=1|PronType=Prs	5	obj	_	SpaceAfter=No\n" +
            "7	?	?	PUNCT	.	_	5	punct	_	SpacesAfter=\\n\n";
        input.setResult(conll1);
        File turn1File = new File("src/test/resources/initial/dla-output_turn0.json");
        testSample(input, turn1File);

        String conll2 = "# sent_id = 2\n" +
            "# text = Yes, I would like to apply for the First Reception Service.\n" +
            "1	Yes	yes	INTJ	UH	_	5	discourse	_	SpaceAfter=No\n" +
            "2	,	,	PUNCT	,	_	5	punct	_	_\n" +
            "3	I	I	PRON	PRP	Case=Nom|Number=Sing|Person=1|PronType=Prs	5	nsubj	_	_\n" +
            "4	would	would	AUX	MD	VerbForm=Fin	5	aux	_	_\n" +
            "5	like	like	VERB	VB	VerbForm=Inf	0	root	_	_\n" +
            "6	to	to	PART	TO	_	7	mark	_	_\n" +
            "7	apply	apply	VERB	VB	VerbForm=Inf	5	xcomp	_	_\n" +
            "8	for	for	ADP	IN	_	12	case	_	_\n" +
            "9	the	the	DET	DT	Definite=Def|PronType=Art	12	det	_	_\n" +
            "10	First	first	ADJ	JJ	Degree=Pos|NumType=Ord	12	amod	_	_\n" +
            "11	Reception	reception	NOUN	NN	Number=Sing	12	compound	_	_\n" +
            "12	Service	service	NOUN	NN	Number=Sing	7	obl	_	SpaceAfter=No\n" +
            "13	.	.	PUNCT	.	_	5	punct	_	SpacesAfter=\\n\n";
        input.setResult(conll2);
        File turn3File = new File("src/test/resources/initial/dla-output_turn1.json");
        testSample(input, turn3File);

        String conll3 = "# sent_id = 3\n" +
            "# text = My name is Karim Y., I come from Syria and I am for 2 months now in Terrassa.\n" +
            "1	My	my	PRON	PRP$	Number=Sing|Person=1|Poss=Yes|PronType=Prs	2	nmod:poss	_	_\n" +
            "2	name	name	NOUN	NN	Number=Sing	4	nsubj	_	_\n" +
            "3	is	be	AUX	VBZ	Mood=Ind|Number=Sing|Person=3|Tense=Pres|VerbForm=Fin	4	cop	_	_\n" +
            "4	Karim	Karim	PROPN	NNP	Number=Sing	0	root	_	_\n" +
            "5	Y.	Y.	PROPN	NNP	Number=Sing	4	flat	_	SpaceAfter=No\n" +
            "6	,	,	PUNCT	,	_	4	punct	_	_\n" +
            "7	I	I	PRON	PRP	Case=Nom|Number=Sing|Person=1|PronType=Prs	8	nsubj	_	_\n" +
            "8	come	come	VERB	VBP	Mood=Ind|Tense=Pres|VerbForm=Fin	4	acl:relcl	_	_\n" +
            "9	from	from	ADP	IN	_	10	case	_	_\n" +
            "10	Syria	Syria	PROPN	NNP	Number=Sing	8	obl	_	_\n" +
            "11	and	and	CCONJ	CC	_	16	cc	_	_\n" +
            "12	I	I	PRON	PRP	Case=Nom|Number=Sing|Person=1|PronType=Prs	16	nsubj	_	_\n" +
            "13	am	be	AUX	VBP	Mood=Ind|Number=Sing|Person=1|Tense=Pres|VerbForm=Fin	16	cop	_	_\n" +
            "14	for	for	ADP	IN	_	16	case	_	_\n" +
            "15	2	2	NUM	CD	NumType=Card	16	nummod	_	_\n" +
            "16	months	month	NOUN	NNS	Number=Plur	8	conj	_	_\n" +
            "17	now	now	ADV	RB	_	16	advmod	_	_\n" +
            "18	in	in	ADP	IN	_	19	case	_	_\n" +
            "19	Terrassa	Terrassa	PROPN	NNP	Number=Sing	16	nmod	_	SpaceAfter=No\n" +
            "20	.	.	PUNCT	.	_	4	punct	_	_\n" +
            "\n" +
            "# sent_id = 4\n" +
            "# text = For the time being I stay with friends.\n" +
            "1	For	for	ADP	IN	_	3	case	_	_\n" +
            "2	the	the	DET	DT	Definite=Def|PronType=Art	3	det	_	_\n" +
            "3	time	time	NOUN	NN	Number=Sing	6	obl	_	_\n" +
            "4	being	be	AUX	VBG	VerbForm=Ger	6	aux	_	_\n" +
            "5	I	I	PRON	PRP	Case=Nom|Number=Sing|Person=1|PronType=Prs	6	nsubj	_	_\n" +
            "6	stay	stay	VERB	VB	VerbForm=Inf	0	root	_	_\n" +
            "7	with	with	ADP	IN	_	8	case	_	_\n" +
            "8	friends	friend	NOUN	NNS	Number=Plur	6	obl	_	SpaceAfter=No\n" +
            "9	.	.	PUNCT	.	_	6	punct	_	SpacesAfter=\\n\n";
        input.setResult(conll3);
        File turn5File = new File("src/test/resources/initial/dla-output_turn2.json");
        testSample(input, turn5File);

        String conll4 = "# sent_id = 5\n" +
            "# text = It is Carrer de Sant Sebastià 66.\n" +
            "1	It	it	PRON	PRP	Case=Nom|Gender=Neut|Number=Sing|Person=3|PronType=Prs	7	nsubj	_	_\n" +
            "2	is	be	AUX	VBZ	Mood=Ind|Number=Sing|Person=3|Tense=Pres|VerbForm=Fin	7	cop	_	_\n" +
            "3	Carrer	Carrer	PROPN	NNP	Number=Sing	5	compound	_	_\n" +
            "4	de	de	PROPN	NNP	Number=Sing	3	flat	_	_\n" +
            "5	Sant	Sant	PROPN	NNP	Number=Sing	2	flat	_	_\n" +
            "6	Sebastià	Sebastià	NOUN	NN	Number=Sing	7	compound	_	_\n" +
            "7	66	66	NUM	CD	NumType=Card	0	root	_	SpaceAfter=No\n" +
            "8	.	.	PUNCT	.	_	7	punct	_	SpacesAfter=\\n\n";
        input.setResult(conll4);
        File turn7File = new File("src/test/resources/initial/dla-output_turn3.json");
        testSample(input, turn7File);
    }    
}
