/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import org.cougaar.core.plugin.*;
import psl.workflakes.exercise.tutorial.assets.*;
import org.cougaar.domain.planning.ldm.asset.*;

public class ProgrammerLDMPlugIn
	extends SimplePlugIn
{

	protected void execute()
	{ }
	
	protected void setupSubscriptions()
	{
		theLDMF.addPropertyGroupFactory(new psl.workflakes.exercise.tutorial.assets.PropertyGroupFactory());	
		ProgrammerAsset new_prototype = (ProgrammerAsset)theLDMF.createPrototype(psl.workflakes.exercise.tutorial.assets.ProgrammerAsset.class, "ProgrammerProto");
		
		new_prototype.setItemIdentificationPG(makeIdentificationPG("Exc4 programmer proto"));
		new_prototype.setLanguagePG(makeLanguagePG(false, false));
		theLDM.cachePrototype("ProgrammerProto", new_prototype);
	
		PropertyGroup langPG;

		ProgrammerAsset firstProgrammer = (ProgrammerAsset) theLDMF.createInstance("ProgrammerProto");
		firstProgrammer.setItemIdentificationPG(makeIdentificationPG("John Brown"));
		langPG = firstProgrammer.getLanguagePG().copy();
		((NewLanguagePG)langPG).setKnowsJava(true);
		firstProgrammer.setLanguagePG(langPG);
		firstProgrammer.setSkillsPG(makeSkillsPG(5, 40));
		publishAdd(firstProgrammer);

		ProgrammerAsset secondProgrammer = (ProgrammerAsset) theLDMF.createInstance("ProgrammerProto");
		secondProgrammer.setItemIdentificationPG(makeIdentificationPG("Mary Riley"));
		langPG = secondProgrammer.getLanguagePG().copy();
		((NewLanguagePG)langPG).setKnowsJava(true);
		((NewLanguagePG)langPG).setKnowsJavaScript(true);
		secondProgrammer.setLanguagePG(langPG);
		secondProgrammer.setSkillsPG(makeSkillsPG(10, 60));
		publishAdd(secondProgrammer);
	}
	
	private LanguagePG makeLanguagePG (boolean knowJava, boolean knowJScript)
	{
		NewLanguagePG aLanguagePG = (NewLanguagePG) theLDMF.createPropertyGroup("LanguagePG");
		aLanguagePG.setKnowsJava(knowJava);
		aLanguagePG.setKnowsJavaScript(knowJScript);
		return aLanguagePG;
	}
	
	private SkillsPG makeSkillsPG (int years, int SLOC)
	{
		NewSkillsPG aSkillsPG = (NewSkillsPG) theLDMF.createPropertyGroup("SkillsPG");
		aSkillsPG.setYearsExperience(years);
		aSkillsPG.setSLOCPerDay(SLOC);
		return aSkillsPG;
	}
	
	private ItemIdentificationPG makeIdentificationPG (String id)
	{
		NewItemIdentificationPG new_item_id_pg = (NewItemIdentificationPG)theLDMF.createPropertyGroup("ItemIdentificationPG");
		new_item_id_pg.setItemIdentification(id);	
		return new_item_id_pg;
	}
}