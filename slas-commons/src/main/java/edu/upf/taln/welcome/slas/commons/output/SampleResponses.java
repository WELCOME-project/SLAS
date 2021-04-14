package edu.upf.taln.welcome.slas.commons.output;

import java.util.ArrayList;
import java.util.List;

import edu.upf.taln.welcome.slas.commons.output.welcome.DlaResult;
import edu.upf.taln.welcome.slas.commons.output.welcome.Entity;
import edu.upf.taln.welcome.slas.commons.output.welcome.Participant;
import edu.upf.taln.welcome.slas.commons.output.welcome.Relation;
import edu.upf.taln.welcome.slas.commons.output.welcome.SpeechAct;
import java.util.Arrays;

/**
 *
 * @author rcarlini
 */
public class SampleResponses {

    public static DeepAnalysisOutput generateIntroduction() {
        
        Entity entity1 = new Entity();
        entity1.setId("entity_1.1");
        entity1.setType("Concept");
        entity1.setAnchor("Hi");
        entity1.setLinks(Arrays.asList(new String[]{"bn:00043620n"}));
        entity1.setConfidence(0.9);

        Entity entity2_1 = new Entity();
        entity2_1.setId("entity_2.1");
        entity2_1.setType("Predicate");
        entity2_1.setAnchor("hear");
        entity2_1.setLinks(Arrays.asList(new String[]{"bn:00089277v"}));
        entity2_1.setConfidence(0.5);

        Entity entity2_2 = new Entity();
        entity2_2.setId("entity_2.2");
        entity2_2.setType("SYSTEM");
        entity2_2.setAnchor("you");
        entity2_2.setConfidence(1.0);

        Entity entity2_3 = new Entity();
        entity2_3.setId("entity_2.3");
        entity2_3.setType("TCN");
        entity2_3.setAnchor("me");
        entity2_3.setConfidence(1.0);

        Participant participant1 = new Participant();
        {        
            participant1.setRoles(Arrays.asList(new String[]{"Agent"}));
            participant1.setEntity(entity2_2.getId());
        }
        
        Participant participant2 = new Participant();
        {        
            participant2.setRoles(Arrays.asList(new String[]{"Object"}));
            participant2.setEntity(entity2_3.getId());
        }
        
        ArrayList<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        
        Relation relation1 = new Relation();
        relation1.setId("relation_1");
        relation1.setPredicate(entity2_1.getId());
        relation1.setParticipants(participants);
        
        SpeechAct speechAct1 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity1.getId());

            speechAct1.setId("act_1");
            speechAct1.setType("Conventional-opening");
            speechAct1.setAnchor("Hi");
            speechAct1.setEntities(entities);
        }
        
        SpeechAct speechAct2 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity2_1.getId());
            entities.add(entity2_2.getId());
            entities.add(entity2_3.getId());
            entities.add(relation1.getId());

            speechAct2.setId("act_2");
            speechAct2.setType("Yes-no question");
            speechAct2.setAnchor("Can you hear me?");
            speechAct2.setEntities(entities);
        }

        List<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2_1);
        entities.add(entity2_2);
        entities.add(entity2_3);
        
        List<Relation> relations = new ArrayList<>();
        relations.add(relation1);
        
        List<SpeechAct> speechActs = new ArrayList<>();
        speechActs.add(speechAct1);
        speechActs.add(speechAct2);
        
        DlaResult data = new DlaResult();
        data.setEntities(entities);
        data.setRelations(relations);
        data.setSpeechActs(speechActs);
        
        DeepAnalysisOutput output = new DeepAnalysisOutput();
        output.setData(data);
        
        return output;
    }
    
    public static DeepAnalysisOutput generateRequest() {
        
        Entity entity1 = new Entity();
        entity1.setId("entity_1.1");
        entity1.setType("Concept");
        entity1.setAnchor("Hello");
        entity1.setLinks(Arrays.asList(new String[]{"bn:00043620n"}));
        entity1.setConfidence(0.9);

        Entity entity2_1 = new Entity();
        entity2_1.setId("entity_2.1");
        entity2_1.setType("Predicate");
        entity2_1.setAnchor("apply");
        entity2_1.setLinks(Arrays.asList(new String[]{"bn:00082707v"}));
        entity2_1.setConfidence(0.8);

        Entity entity2_2 = new Entity();
        entity2_2.setId("entity_2.2");
        entity2_2.setType("TCN");
        entity2_2.setAnchor("I");
        entity2_2.setConfidence(1.0);

        Entity entity2_3 = new Entity();
        entity2_3.setId("entity_2.3");
        entity2_3.setType("Concept");
        entity2_3.setAnchor("Service");
        entity2_3.setLinks(Arrays.asList(new String[]{"bn:00070651n"}));
        entity2_3.setConfidence(0.8);

        Entity entity2_4 = new Entity();
        entity2_4.setId("entity_2.4");
        entity2_4.setType("Concept");
        entity2_4.setAnchor("First Reception Service");
        entity2_4.setConfidence(0.7);


        Participant participant1 = new Participant();
        {        
            participant1.setRoles(Arrays.asList(new String[]{"Agent"}));
            participant1.setEntity(entity2_2.getId());
        }
        
        Participant participant2 = new Participant();
        {        
            participant2.setRoles(Arrays.asList(new String[]{"Object"}));
            participant2.setEntity(entity2_3.getId());
        }
        
        ArrayList<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        
        Relation relation1 = new Relation();
        relation1.setId("relation_1");
        relation1.setPredicate(entity2_1.getId());
        relation1.setParticipants(participants);
        
        SpeechAct speechAct1 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity1.getId());

            speechAct1.setId("act_1");
            speechAct1.setType("Conventional-opening");
            speechAct1.setAnchor("Hello");
            speechAct1.setEntities(entities);
        }
        
        SpeechAct speechAct2 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity2_1.getId());
            entities.add(entity2_2.getId());
            entities.add(entity2_3.getId());
            entities.add(entity2_4.getId());
            entities.add(relation1.getId());

            speechAct2.setId("act_2");
            speechAct2.setAnchor("I would like to apply for the First Reception Service");
            speechAct2.setType("Commit");
            speechAct2.setEntities(entities);
        }


        List<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2_1);
        entities.add(entity2_2);
        entities.add(entity2_3);
        entities.add(entity2_4);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation1);
        
        List<SpeechAct> speechActs = new ArrayList<>();
        speechActs.add(speechAct1);
        speechActs.add(speechAct2);
        
        
        DlaResult data = new DlaResult();
        data.setEntities(entities);
        data.setRelations(relations);
        data.setSpeechActs(speechActs);
        
        DeepAnalysisOutput output = new DeepAnalysisOutput();
        output.setData(data);
        return output;
    }
    
    public static DeepAnalysisOutput generateKarimInfo() {
        
        Entity entity1_1 = new Entity();
        entity1_1.setId("entity_1.1");
        entity1_1.setType("TCN");
        entity1_1.setConfidence(1.0);
        
        Entity entity1_2 = new Entity();
        entity1_2.setId("entity_1.2");
        entity1_2.setType("Predicate");
        entity1_2.setAnchor("name");
        entity1_2.setLinks(Arrays.asList(new String[]{"bn:00056758n"}));
        entity1_2.setConfidence(1.0);
        
        Entity entity1_3 = new Entity();
        entity1_3.setId("entity_1.3");
        entity1_3.setType("Person");
        entity1_3.setAnchor("Karim Y");
        entity1_3.setConfidence(0.2);

        Entity entity2_1 = new Entity();
        entity2_1.setId("entity_2.1");
        entity2_1.setType("Predicate");
        entity2_1.setAnchor("come");
        entity2_1.setLinks(Arrays.asList(new String[]{"bn:00082788v"}));
        entity2_1.setConfidence(0.8);

        Entity entity2_2 = new Entity();
        entity2_2.setId("entity_2.2");
        entity2_2.setType("Location");
        entity2_2.setAnchor("Syria");
        entity2_2.setLinks(Arrays.asList(new String[]{"bn:00075752n"}));
        entity2_2.setConfidence(0.9);

        Entity entity3_1 = new Entity();
        entity3_1.setId("entity_3.1");
        entity3_1.setType("Predicate");
        entity3_1.setAnchor("am");
        entity3_1.setLinks(Arrays.asList(new String[]{"bn:00083183v"}));
        entity3_1.setConfidence(0.7);

        Entity entity3_2 = new Entity();
        entity3_2.setId("entity_3.2");
        entity3_2.setType("Time");
        entity3_2.setAnchor("for two months now");
        entity3_2.setConfidence(0.6);

        Entity entity3_3 = new Entity();
        entity3_3.setId("entity_3.3");
        entity3_3.setType("Location");
        entity3_3.setAnchor("Terrassa");
        entity3_3.setLinks(Arrays.asList(new String[]{"bn:03554826n"}));
        entity3_3.setConfidence(1.0);

        Entity entity4_1 = new Entity();
        entity4_1.setId("entity_4.1");
        entity4_1.setType("Predicate");
        entity4_1.setAnchor("stay");
        entity4_1.setLinks(Arrays.asList(new String[]{"bn:00082137v"}));
        entity4_1.setConfidence(0.7);

        Entity entity4_2 = new Entity();
        entity4_2.setId("entity_4.2");
        entity4_2.setType("Time");
        entity4_2.setAnchor("for the time being");
        entity4_2.setConfidence(0.6);

        Entity entity4_3 = new Entity();
        entity4_3.setId("entity_4.3");
        entity4_3.setType("Location");
        entity4_3.setAnchor("friends");
        entity4_3.setLinks(Arrays.asList(new String[]{"bn:00036538n"}));
        entity4_3.setConfidence(0.3);

        Relation relation1 = new Relation();
        {
            Participant participant1 = new Participant();
            {        
                participant1.setRoles(Arrays.asList(new String[]{"Agent"}));
                participant1.setEntity(entity1_1.getId());
            }

            Participant participant2 = new Participant();
            {        
                participant2.setRoles(Arrays.asList(new String[]{"Object"}));
                participant2.setEntity(entity1_3.getId());
            }

            ArrayList<Participant> participants = new ArrayList<>();
            participants.add(participant1);
            participants.add(participant2);

            relation1.setId("relation_1");
            relation1.setPredicate(entity1_2.getId());
            relation1.setParticipants(participants);
        }

        Relation relation2 = new Relation();
        {
            Participant participant1 = new Participant();
            {        
                participant1.setRoles(Arrays.asList(new String[]{"Agent"}));
                participant1.setEntity(entity1_1.getId());
            }

            Participant participant2 = new Participant();
            {        
                participant2.setRoles(Arrays.asList(new String[]{"Location"}));
                participant2.setEntity(entity2_2.getId());
            }

            ArrayList<Participant> participants = new ArrayList<>();
            participants.add(participant1);
            participants.add(participant2);

            relation2.setId("relation_2");
            relation2.setPredicate(entity2_1.getId());
            relation2.setParticipants(participants);
        }

        Relation relation3 = new Relation();
        {
            Participant participant1 = new Participant();
            {        
                 participant1.setRoles(Arrays.asList(new String[]{"Agent"}));
                participant1.setEntity(entity1_1.getId());
            }

            Participant participant2 = new Participant();
            {        
                participant2.setRoles(Arrays.asList(new String[]{"Time"}));
                participant2.setEntity(entity3_2.getId());
            }

            Participant participant3 = new Participant();
            {        
                participant3.setRoles(Arrays.asList(new String[]{"Place"}));
                participant3.setEntity(entity3_3.getId());
            }

            ArrayList<Participant> participants = new ArrayList<>();
            participants.add(participant1);
            participants.add(participant2);
            participants.add(participant3);

            relation3.setId("relation_3");
            relation3.setPredicate(entity3_1.getId());
            relation3.setParticipants(participants);
        }

        Relation relation4 = new Relation();
        {
            Participant participant1 = new Participant();
            {        
                participant1.setRoles(Arrays.asList(new String[]{"Agent"}));
                participant1.setEntity(entity1_1.getId());
            }

            Participant participant2 = new Participant();
            {        
                participant2.setRoles(Arrays.asList(new String[]{"Time"}));
                participant2.setEntity(entity4_1.getId());
            }

            Participant participant3 = new Participant();
            {        
                participant3.setRoles(Arrays.asList(new String[]{"Place"}));
                participant3.setEntity(entity4_3.getId());
            }

            ArrayList<Participant> participants = new ArrayList<>();
            participants.add(participant1);
            participants.add(participant2);
            participants.add(participant3);

            relation4.setId("relation_4");
            relation4.setPredicate(entity4_2.getId());
            relation4.setParticipants(participants);
        }
        
        SpeechAct speechAct1 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity1_1.getId());
            entities.add(entity1_2.getId());
            entities.add(entity1_3.getId());
            entities.add(relation1.getId());

            speechAct1.setId("act_1");
            speechAct1.setType("Non opinion statement");
            speechAct1.setAnchor("My name is Karim Y");
            speechAct1.setEntities(entities);
        }
        
        SpeechAct speechAct2 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity2_1.getId());
            entities.add(entity2_2.getId());
            entities.add(relation2.getId());

            speechAct2.setId("act_2");
            speechAct2.setType("Non opinion statement");
            speechAct2.setAnchor("I come from Syria");
            speechAct2.setEntities(entities);
        }
        
        SpeechAct speechAct3 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity3_1.getId());
            entities.add(entity3_2.getId());
            entities.add(entity3_3.getId());
            entities.add(relation3.getId());

            speechAct3.setId("act_3");
            speechAct3.setType("Non opinion statement");
            speechAct3.setAnchor("I am for 2 months now in Terrassa");
            speechAct3.setEntities(entities);
        }
        
        SpeechAct speechAct4 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity4_1.getId());
            entities.add(entity4_2.getId());
            entities.add(entity4_3.getId());
            entities.add(relation4.getId());

            speechAct4.setId("act_4");
            speechAct4.setType("Non opinion statement");
            speechAct4.setAnchor("For the time being I stay with friends");
            speechAct4.setEntities(entities);
        }

        List<Entity> entities = new ArrayList<>();
        entities.add(entity1_1);
        entities.add(entity1_2);
        entities.add(entity1_3);
        entities.add(entity2_1);
        entities.add(entity2_2);
        entities.add(entity3_1);
        entities.add(entity3_2);
        entities.add(entity3_3);
        entities.add(entity4_1);
        entities.add(entity4_2);
        entities.add(entity4_3);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation1);
        relations.add(relation2);
        relations.add(relation3);
        relations.add(relation4);
        
        List<SpeechAct> speechActs = new ArrayList<>();
        speechActs.add(speechAct1);
        speechActs.add(speechAct2);
        speechActs.add(speechAct3);
        speechActs.add(speechAct4);
        
        DlaResult data = new DlaResult();
        data.setEntities(entities);
        data.setRelations(relations);
        data.setSpeechActs(speechActs);
        
        DeepAnalysisOutput output = new DeepAnalysisOutput();
        output.setData(data);        
        return output;        
    }
    
    public static DeepAnalysisOutput generateAddress() {
        
        Entity entity1_1 = new Entity();
        entity1_1.setId("entity_1.1");
        entity1_1.setType("Concept");
        entity1_1.setAnchor("it");
        entity1_1.setConfidence(1.0);
        
        Entity entity1_2 = new Entity();
        entity1_2.setId("entity_1.2");
        entity1_2.setType("Predicate");
        entity1_2.setAnchor("is");
        entity1_2.setLinks(Arrays.asList(new String[]{"bn:00056758n"}));
        entity1_2.setConfidence(1.0);
        
        Entity entity1_3 = new Entity();
        entity1_3.setId("entity_1.3");
        entity1_3.setType("Location");
        entity1_3.setAnchor("Carrer de Sant Sebastià, 66");
        entity1_3.setLinks(Arrays.asList(new String[]{"osm:39524296"}));
        entity1_3.setConfidence(0.2);

        Relation relation1 = new Relation();
        {
            Participant participant1 = new Participant();
            {        
                participant1.setRoles(Arrays.asList(new String[]{"Agent"}));
                participant1.setEntity(entity1_1.getId());
            }

            Participant participant2 = new Participant();
            {        
                participant2.setRoles(Arrays.asList(new String[]{"Object"}));
                participant2.setEntity(entity1_3.getId());
            }

            ArrayList<Participant> participants = new ArrayList<>();
            participants.add(participant1);
            participants.add(participant2);

            relation1.setId("relation_1");
            relation1.setPredicate(entity1_2.getId());
            relation1.setParticipants(participants);
        }
        
        SpeechAct speechAct1 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity1_1.getId());
            entities.add(entity1_2.getId());
            entities.add(entity1_3.getId());
            entities.add(relation1.getId());

            speechAct1.setId("act_1");
            speechAct1.setType("Non opinion statement");
            speechAct1.setAnchor("It is Carrer de Sant Sebastià 66.");
            speechAct1.setEntities(entities);
        }

        List<Entity> entities = new ArrayList<>();
        entities.add(entity1_1);
        entities.add(entity1_2);
        entities.add(entity1_3);

        List<Relation> relations = new ArrayList<>();
        relations.add(relation1);
        
        List<SpeechAct> speechActs = new ArrayList<>();
        speechActs.add(speechAct1);
        
        DlaResult data = new DlaResult();
        data.setEntities(entities);
        data.setRelations(relations);
        data.setSpeechActs(speechActs);
        
        DeepAnalysisOutput output = new DeepAnalysisOutput();
        output.setData(data);        
        return output;        
    }
    
    public static DeepAnalysisOutput generateResponse(int turn) {
        
        DeepAnalysisOutput output;
        switch(turn) {
            case 1:
                output = generateIntroduction();
                break;
            case 3:
                output = generateRequest();
                break;
            case 5:
                output = generateKarimInfo();
                break;
            case 7:
                output = generateAddress();
                break;
            default:
                output = generateIntroduction();
                break;
        }
        
        return output;
        
    }
    
}
