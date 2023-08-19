package j.cpp;

import java.util.List;
public class Init
	{
	private int type;
	private boolean valueBool;
	private int valueInteger;
	private float valueFloat;
	private String valueString;
	private String name;
	private boolean hasValue;
	private boolean global;
	private int lineFrom;
	private int lineTo;
	private CValueOperator classType;
	private String canBe = "1234567890_qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	public Init()
		{
		// for create
		}
	public Init(String tempName)
		{
		name = tempName;
		global = true;
		}
	public Init(String newName, int newType)
		{
		this.setName(newName);
		type = newType;
		}
	public Init(boolean isArray, String newName, int newType)
		{
		name = newName;
		type = newType;
		}
	public Init(String newName, int newType, boolean newValue)
		{
		this.setName(newName);
		type = newType;
		this.setValueBool(newValue);
		}	
	public Init(String newName, int newType, int newValue)
		{
		this.setName(newName);
		type = newType;
		this.setValueInteger(newValue);
		}
	public Init(String newName, int newType, float newValue)
		{
		this.setName(newName);
		type = newType;
		this.setValueFloat(newValue);
		}
	public String getStringType()
		{
		if (this.getType() == Type.BOOLEAN) return "bool";
		else if (this.getType() == Type.INTEGER) return "int";
		else return "float";
		}					
	public int getType()
		{
		return type;
		}	
	public void setType(int newType)
		{
		type = newType;
		}
	public boolean getValueBool()
		{
		return valueBool;
		}
	public void setValueBool(boolean newValueBool)
		{
		if (this.getType() != Type.BOOLEAN) throw new JCppBadTypeException("!!!!!!!!!!!z�y typ");
		valueBool = newValueBool;
		valueString = String.valueOf(valueBool);
		this.setHasValue(true);
		}
	public int getValueInteger()
		{
		return valueInteger;
		}
	public void setValueInteger(int newValueInteger)
		{
		if (this.getType() != Type.INTEGER) throw new JCppBadTypeException("!!!!!!!!!!!z�y typ");
		valueInteger = newValueInteger;
		valueString = String.valueOf(valueInteger);
		this.setHasValue(true);
		}
	public float getValueFloat()
		{
		return valueFloat;
		}
	public void setValueFloat(float newValueFloat)
		{
		if (this.getType() != Type.FLOAT) throw new JCppBadTypeException("!!!!!!!!!!!z�y typ");
		valueFloat = newValueFloat;
		valueString = String.valueOf(valueFloat);
		this.setHasValue(true);
		}
	public String getValueString()
		{
		return valueString;
		}
	public void setValueString(String newValueString)
		{
		valueString = newValueString;
		}		
	public String getName()
		{
		return name;
		}			
	public void setName(String newName)
		{
		if (newName.length() == 0) throw new JCppBadVariableNameException("!!!!!!!!!!!!!d�ugo�� nazwy 0");
		for (int i=0;i<10;i++) if (newName.indexOf(String.valueOf(i)) == 0) throw new JCppBadVariableNameException("!!!!!!!!!nie od cyfry");
		int nameCharsOk = 0;
		for (int i=0;i<newName.length();i++)
			{
			for (int j=0;j<canBe.length();j++)
				{
				if (newName.charAt(i) == canBe.charAt(j)) nameCharsOk++;
				}
			}
		if (nameCharsOk < newName.length() && !(newName.indexOf("*temp") == 0)) throw new JCppBadVariableNameException("!!!!!!!!!!!!!niew�a�ciwa nazwa");
		name = newName;
		}		
	public boolean getHasValue()
		{
		return hasValue;
		}
	public void setHasValue(boolean newHasValue)
		{
		hasValue = newHasValue;
		}
	public boolean isGlobal()
		{
		return global;
		}
	public void setGlobal(boolean newGlobal)
		{
		global = newGlobal;
		}
	public int getLineFrom()
		{
		return lineFrom;
		}				
	public void setLineFrom(int newLineFrom)
		{
		lineFrom = newLineFrom;
		}
	public int getLineTo()
		{
		return lineTo;
		}				
	public void setLineTo(int newLineTo)
		{
		lineTo = newLineTo;
		}
	public CValueOperator getClassType()
		{
		if (this.getType() == Type.BOOLEAN) return new CBoolean();
		if (this.getType() == Type.INTEGER) return new CInteger();
		if (this.getType() == Type.FLOAT) return new CFloat();
		return null;
		}
	public static Init[] doInitArray(String text, int lineFrom, int lineTo, List initList)
		{
		Init []init;
		JCppUtil jCppUtil = new JCppUtil();
		text = text.trim();
		int newType;
		if (text.indexOf("int ") == 0) newType = Type.INTEGER;
		else if (text.indexOf("float ") == 0) newType = Type.FLOAT;
		else if (text.indexOf("bool ") == 0) newType = Type.BOOLEAN;
		else throw new JCppBadStatementException("!!!!!!!!!!!!!!!!!!!!!!!!!!!nie w�a�ciwy typ");
		text = text.substring(text.indexOf(" "), text.length());
		if (text.length() == 1) throw new JCppBadStatementException("!!!!!!!!!!!!!!!!!!!!!!!!!!!brak nazwy");
		text = text.trim();
		String nameArray = text.substring(0, text.indexOf(Operator.KWADRAT_LEFT));
		String howMany = text.substring(text.indexOf(Operator.KWADRAT_LEFT));
		howMany = JCppUtil.getLost(howMany, Operator.KWADRAT_LEFT);
		howMany = JCppUtil.getLost(howMany, Operator.KWARDAT_RIGHT);
		System.out.println(howMany + "KKKKKKKKKKK");
		Sentence sentence = new Sentence(howMany, initList, lineFrom);
		if (jCppUtil.getType(sentence.getValueString()) != Type.INTEGER)
			{
			throw new JCppBadTypeException("!!!!!!!!! typem powinien by� int");
			}
		int number = Integer.parseInt(sentence.getValueString());
		if (number > 0)
			{
			init = new Init[number];
			Init initOne;
			for (int i=0;i<number;i++)
				{
				initOne = new Init(true, nameArray + "[" + i + "]", newType);
				System.out.println(initOne.getName() + ",,,");
				initOne.setLineFrom(lineFrom);
				initOne.setLineTo(lineTo);
				init[i] = initOne;
				}
			return init;
			}
		throw new JCppOutOfRangeException("!!!!!!!! ilo�� element�w tablicy powinna by� wi�ksza od 0");
		}
	public static Init doInit(String text, int lineFrom, int lineTo, List initList)	//ze �rednikiem
		{
		Init init;
		JCppUtil jCppUtil = new JCppUtil();
		text = text.trim();
		int newType;
		if (text.indexOf("int ") == 0) newType = Type.INTEGER;
		else if (text.indexOf("float ") == 0) newType = Type.FLOAT;
		else if (text.indexOf("bool ") == 0) newType = Type.BOOLEAN;
		else throw new JCppBadStatementException("!!!!!!!!!!!!!!!!!!!!!!!!!!!nie w�a�ciwy typ");
		text = text.substring(text.indexOf(" "), text.length());
		if (text.length() == 1) throw new JCppBadStatementException("!!!!!!!!!!!!!!!!!!!!!!!!!!!brak nazwy");
		String end;
		if (text.indexOf("=") != -1) end = "=";
		else if (text.indexOf(";") != -1) end = ";";
		else throw new JCppBadStatementException("!!!!!!!!!!!!!!!!!!!!!!!!!!!brak \";\" lub \"=\" ");		
		String temp = text.substring(0, text.indexOf(end));
		temp = temp.trim();
		//this.setName(temp);
		//System.out.println(temp + "*");
		init = new Init(temp, newType);
		init.setLineFrom(lineFrom);
		init.setLineTo(lineTo);
		if (text.indexOf("=") != -1)
			{
			temp = text.substring(text.indexOf("=") + 1, text.indexOf(";"));
			temp = temp.trim();
			System.out.println(temp);
			switch (jCppUtil.getType(temp))
				{
				case -1:
					//
					Init initTemp = null;
					boolean was = false;
					for (int i=0;i<initList.size();i++)
						{
						initTemp = (Init)initList.get(i);
						if (initTemp.getName().equals(temp))
							{
							if ((initTemp.getLineFrom() < lineFrom && initTemp.getLineTo() >= lineTo) || initTemp.isGlobal())
								{
								if (!initTemp.getHasValue()) throw new JCppHasNoValueException("!!!!!!!!!!!nie ma warto�ci");
								was = true;
								break;
								}
							}
						}
					if (was)
						{
						if (initTemp.getType() == newType || (newType == Type.FLOAT && initTemp.getType() == Type.INTEGER))
							{
							if (init.getType() == Type.BOOLEAN)	init.setValueBool(initTemp.getValueBool());
							if (init.getType() == Type.INTEGER) init.setValueInteger(initTemp.getValueInteger());
							if (init.getType() == Type.FLOAT)
								{
								System.out.println("tuuuuuuu");
								if (initTemp.getType() == Type.FLOAT) init.setValueFloat(initTemp.getValueFloat());
								else if (initTemp.getType() == Type.INTEGER) init.setValueFloat(initTemp.getValueInteger());
								else throw new JCppDifferentTypesException("!!!!!!!!!!niew�a�ciwy typ");
								}
							}
						else throw new JCppDifferentTypesException("!!!!!!!!!!niew�a�ciwy typ");
						}
					else throw new JCppNotInitException("!!!!!!!!!!!!!!!!!!nie zainicjowana zmienna");
					break;
				case Type.BOOLEAN:
					if (newType == Type.BOOLEAN) init.setValueBool(new Boolean(temp).booleanValue());
						else throw new JCppBadTypeException("!!!!!!!!!!!!niew�a�ciwy typ");
					break;
				case Type.INTEGER:
					if (newType != Type.BOOLEAN)
						{
						if (newType == Type.INTEGER) init.setValueInteger(new Integer(temp).intValue());
							else if (newType == Type.FLOAT) init.setValueFloat(new Integer(temp).intValue());
						}
					else throw new JCppBadTypeException("!!!!!!!!!!!!niew�a�ciwy typ");
					break;
				case Type.FLOAT:
					if (newType == Type.FLOAT) init.setValueFloat(new Float(temp).floatValue());
						else throw new JCppBadTypeException("!!!!!!!!!!!!niew�a�ciwy typ");
					break;
				}
			}
		if (init.getHasValue())
			{
			if (init.getType() == Type.BOOLEAN) init.setValueString(String.valueOf(init.getValueBool()));
			if (init.getType() == Type.INTEGER) init.setValueString(String.valueOf(init.getValueInteger()));
			if (init.getType() == Type.FLOAT) init.setValueString(String.valueOf(init.getValueFloat()));
			}
		return init;
		}
	/*private String findEndName(String text)
		{
		if (text.indexOf("=") != -1) return "=";
		if (text.indexOf(";") != -1) return ";";
		else throw new JCppBadStatementException("!!!!!!!!!!!!!!!!!!!!!!!!!!!brak \";\" lub \"=\" ");
		}*/
	public static void main(String[] argv)
		{
		//Init init = new Init("t_1qqqa", Type.INTEGER, 10);
		List list = new java.util.ArrayList();
		Init jeden = new Init("jeden", Type.INTEGER, 1);
		jeden.setGlobal(true);
		Init dwa = new Init("dwa", Type.BOOLEAN, true);
		dwa.setGlobal(true);
		list.add(jeden);
		list.add(dwa);				
		Init init = Init.doInit("  float iiiu_8 = jeden;", 4, 5, list);
		System.out.println(init.getType());
		System.out.println(init.getName());
		if (init.getType() == Type.BOOLEAN) System.out.println(init.getValueBool());
		if (init.getType() == Type.INTEGER) System.out.println(init.getValueInteger());
		if (init.getType() == Type.FLOAT) System.out.println(init.getValueFloat());				
		//init.doInit("int x = 5;", 4, 5);
		}
	}