/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.planning.ldm.asset.*; 
import java.util.Vector;

public class ManagerPlugIn extends SimplePlugIn {


protected void setupSubscriptions()
{
	// asset definitin for task "CODE"
	// create prototype
	Asset programProto = theLDMF.createPrototype("AbstractAsset", "ProgramProto");
	NewItemIdentificationPG iipg = (NewItemIdentificationPG)theLDMF.createPropertyGroup("ItemIdentificationPG");
  	iipg.setItemIdentification("Exc4 program proto");
  	theLDM.cachePrototype("ProgramProto", programProto);
    
    // create a couple of task "CODE" instances
    Asset what_to_code, what_else_to_code;
    Vector prepositions = new Vector();
    Vector preferences = new Vector();
    
    //create a program Asset from Prototype
    what_to_code = (Asset)theLDMF.createInstance("ProgramProto");
    iipg = (NewItemIdentificationPG)theLDMF.createPropertyGroup("ItemIdentificationPG");
    iipg.setItemIdentification("The Next Killer App");
    what_to_code.setItemIdentificationPG(iipg);
    publishAdd(what_to_code);
    //create another program Asset from Prototype
    what_else_to_code = (Asset)theLDMF.createInstance("ProgramProto");
    iipg = (NewItemIdentificationPG)theLDMF.createPropertyGroup("ItemIdentificationPG");
    iipg.setItemIdentification("Something in Java");
    what_else_to_code.setItemIdentificationPG(iipg);
    publishAdd(what_else_to_code);
    
    // setting up prep. phrase
    NewPrepositionalPhrase prep = theLDMF.newPrepositionalPhrase();
    prep.setPreposition("USING_LANGUAGE");
    prep.setIndirectObject("Java");
    prepositions.add(prep);
         
	// creation of Task instances with verb "CODE"

	//setting up preferences
    AspectValue startMonth = new AspectValue(org.cougaar.domain.planning.ldm.plan.AspectType.START_TIME, 1);
    AspectValue endMonth = new AspectValue(org.cougaar.domain.planning.ldm.plan.AspectType.END_TIME, 5);
    ScoringFunction scoreFunc = ScoringFunction.createStrictlyBetweenValues(startMonth, endMonth);
    Preference pref = theLDMF.newPreference (org.cougaar.domain.planning.ldm.plan.AspectType.START_TIME, scoreFunc);
    preferences.add(pref);
    AspectValue duration = new AspectValue(org.cougaar.domain.planning.ldm.plan.AspectType.DURATION, 3);
	scoreFunc = ScoringFunction.createStrictlyAtValue(duration);
	pref = theLDMF.newPreference (org.cougaar.domain.planning.ldm.plan.AspectType.DURATION, scoreFunc);
	preferences.add(pref);

	NewTask new_task = theLDMF.newTask();
	new_task.setVerb(Verb.getVerb("CODE"));
	new_task.setPlan(theLDMF.getRealityPlan());
	new_task.setDirectObject(what_to_code);	
	new_task.setPrepositionalPhrases(prepositions.elements());
	new_task.setPreferences(preferences.elements());
	publishAdd(new_task);
	preferences.clear();
	
	//setting up preferences
	startMonth = new AspectValue(org.cougaar.domain.planning.ldm.plan.AspectType.START_TIME, 0);
    endMonth = new AspectValue(org.cougaar.domain.planning.ldm.plan.AspectType.END_TIME, 4);
    scoreFunc = ScoringFunction.createStrictlyBetweenValues(startMonth, endMonth);
    pref = theLDMF.newPreference (org.cougaar.domain.planning.ldm.plan.AspectType.START_TIME, scoreFunc);
	preferences.add(pref);
    duration = new AspectValue(org.cougaar.domain.planning.ldm.plan.AspectType.DURATION, 3);
	scoreFunc = ScoringFunction.createStrictlyAtValue(duration);
	pref = theLDMF.newPreference (org.cougaar.domain.planning.ldm.plan.AspectType.DURATION, scoreFunc);
	preferences.add(pref);
	
	new_task = theLDMF.newTask();
	new_task.setVerb(Verb.getVerb("CODE"));
	new_task.setPlan(theLDMF.getRealityPlan());
	new_task.setDirectObject(what_else_to_code);
	new_task.setPrepositionalPhrases(prepositions.elements());
	new_task.setPreferences(preferences.elements());	
	publishAdd(new_task);
	preferences.clear();
	
	System.out.println("ManagerPlugIn");
}

/**
 * This plugin has no subscriptions so execute does nothing
 */
protected void execute () {}
} 
