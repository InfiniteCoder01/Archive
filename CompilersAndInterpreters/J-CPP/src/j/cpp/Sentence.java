package j.cpp;

import java.util.List;
public class Sentence
	{
	private int typeFirst;
	private int typeSecond;
	private String operator;
	private boolean value;
	private boolean valueBool;
	private int valueInteger;
	private float valueFloat;	
	private boolean valueBoolFirst;
	private int valueIntegerFirst;
	private float valueFloatFirst;
	private boolean valueBoolSecond;
	private int valueIntegerSecond;
	private float valueFloatSecond;
	private String text;
	private String nameFirst;
	private String nameSecond;
	private List list;
	private int type;
	private String valueString;
	public Sentence(String newText, List newInitList, int newLineNumber)
		{
		text = newText;
		doSentence(newText, newInitList, newLineNumber);
		}
	public Sentence(String newText, List newInitList, int newLineNumber, boolean small)
		{
		text = newText;
		doSentenceSmall(newText, newInitList, newLineNumber);
		}		
	public int getType()
		{
		return type;
		}
	public void setType(int newType)
		{
		type = newType;
		}			
	public int getTypeFirst()
		{
		return typeFirst;
		}
	public void setTypeFirst(int newTypeFirst)
		{
		typeFirst = newTypeFirst;
		}
	public int getTypeSecond()
		{
		return typeSecond;
		}
	public void setTypeSecond(int newTypeSecond)
		{
		typeSecond = newTypeSecond;
		}
	public String getOperator()
		{
		return operator;
		}		
	public void setOperator(String newOperator)
		{
		operator = newOperator;
		}
	public boolean getValue()
		{
		return value;
		}
	public void setValue(boolean newValue)
		{
		value = newValue;
		}		
	public boolean getValueBool()
		{
		return valueBool;
		}
	public void setValueBool(boolean newValueBool)
		{
		valueBool = newValueBool;
		}
	public int getValueInteger()
		{
		return valueInteger;
		}
	public void setValueInteger(int newValueInteger)
		{
		valueInteger = newValueInteger;
		}
	public float getValueFloat()
		{
		return valueFloat;
		}
	public void setValueFloat(float newValueFloat)
		{
		valueFloat = newValueFloat;
		}		
	public boolean getValueBoolFirst()
		{
		return valueBoolFirst;
		}
	public void setValueBoolFirst(boolean newValueBoolFirst)
		{
		valueBoolFirst = newValueBoolFirst;
		}
	public int getValueIntegerFirst()
		{
		return valueIntegerFirst;
		}
	public void setValueIntegerFirst(int newValueIntegerFirst)
		{
		valueIntegerFirst = newValueIntegerFirst;
		}
	public float getValueFloatFirst()
		{
		return valueFloatFirst;
		}
	public void setValueFloatFirst(float newValueFloatFirst)
		{
		valueFloatFirst = newValueFloatFirst;
		}		
	public boolean getValueBoolSecond()
		{
		return valueBoolSecond;
		}
	public void setValueBoolSecond(boolean newValueBoolSecond)
		{
		valueBoolSecond = newValueBoolSecond;
		}
	public int getValueIntegerSecond()
		{
		return valueIntegerSecond;
		}
	public void setValueIntegerSecond(int newValueIntegerSecond)
		{
		valueIntegerSecond = newValueIntegerSecond;
		}
	public float getValueFloatSecond()
		{
		return valueFloatSecond;
		}
	public void setValueFloatSecond(float newValueFloatSecond)
		{
		valueFloatSecond = newValueFloatSecond;
		}
	public String getNameFirst()
		{
		return nameFirst;
		}
	public void setNameFirst(String newNameFirst)
		{
		nameFirst = newNameFirst;
		}
	public String getNameSecond()
		{
		return nameSecond;
		}
	public void setNameSecond(String newNameSecond)
		{
		nameSecond = newNameSecond;
		}
	public List getList()
		{
		return list;
		}
	public void setList(List newList)
		{
		list = newList;
		}
	public String getValueString()
		{
		return valueString;
		}
	public void setValueString(String newValueString)
		{
		valueString = newValueString;
		}
	public boolean doSentenceSmall(String newText, List newInitList, int newLineNumber)
		{
		Init initTemp = null;
		int placeList = -1;
		newText = JCppUtil.getLost(newText, "(");
		newText = JCppUtil.getLost(newText, ")");
		newText = JCppUtil.getLost(newText, " ");
		newText = JCppUtil.getLost(newText, ";");
		boolean plus = false;
		boolean minus = false;
		boolean was = false;
		if (newText.indexOf("++") > 0)
			{
			plus = true;
			newText = JCppUtil.getLost(newText, "++");
			}
		if (newText.indexOf("--") > 0)
			{
			minus = true;
			newText = JCppUtil.getLost(newText, "--");
			}			
		if (plus && minus) throw new JCppBadStatementException("!!!!!!!!!!!z�a sk�adnia");
		for (int i=0;i<newInitList.size();i++)
			{
			initTemp = (Init)newInitList.get(i);
			if (initTemp.getName().equals(newText))
				{
				if ((initTemp.getLineFrom() <= newLineNumber && initTemp.getLineTo() >= newLineNumber) || initTemp.isGlobal())
					{
					if (!initTemp.getHasValue()) throw new JCppHasNoValueException("!!!!!!!!!!!nie ma warto�ci");
					was = true;
					placeList = i;
					break;
					}
				}
			}
		if (was)
			{
			if (initTemp.getType() == Type.INTEGER)
				{
				if (plus) initTemp.setValueInteger(initTemp.getValueInteger() + 1);
				if (minus) initTemp.setValueInteger(initTemp.getValueInteger() - 1);
				}
			if (initTemp.getType() == Type.FLOAT)
				{
				if (plus) initTemp.setValueFloat(initTemp.getValueFloat() + 1);
				if (minus) initTemp.setValueFloat(initTemp.getValueFloat() - 1);
				}
			newInitList.set(placeList, initTemp);				
			}
		else throw new JCppNotInitException("!!!!!!!!!!!nie zainicjowana zmienna");
		this.setList(newInitList);
		return was;
		}
	public boolean doSentence(String newText, List initList, int lineNumber)
		{
		boolean out = false;
		newText = JCppUtil.getLost(newText, "(");
		newText = JCppUtil.getLost(newText, ")");
		newText = JCppUtil.getLost(newText, " ");
		newText = JCppUtil.getLost(newText, ";");		
		JCppUtil jCppUtil = new JCppUtil();
		String[] operators = jCppUtil.getSentenceOperators();
		boolean was = false;
		int bestPlace = newText.length();
		for (int i=0;i<operators.length;i++)
			{
			if (newText.indexOf(operators[i], 1) != -1)
				{
				if (newText.indexOf(operators[i], 1) < bestPlace)
					{
					bestPlace = newText.indexOf(operators[i], 1);
					operator = operators[i];
					was = true;						
					}
				}
			}
		if (was)	//operator
			{
			out = true;
			boolean minusFirst = false, minusSecond = false, notFirst = false, notSecond = false;
			Init initFirst = null;
			Init initSecond = null;			
			if (newText.indexOf(operator) > 0)
				{
				String temp = newText.substring(0, newText.indexOf(operator));
				if (temp.indexOf("-") == 0)
					{
					minusFirst = true;
					temp = jCppUtil.getLost(temp, "-");
					}
				if (temp.indexOf("!") == 0)
					{
					notFirst = true;
					temp = jCppUtil.getLost(temp, "!");
					}
				switch (jCppUtil.getType(temp))
					{
					case -1:
					this.setNameFirst(temp);
					break;
					case Type.BOOLEAN:
					this.setTypeFirst(Type.BOOLEAN);
					this.setNameFirst("*temp");
					this.setValueBoolFirst(new Boolean(temp).booleanValue());
					//	else this.setValueBoolFirst(!(new Boolean(temp).booleanValue()));
					//if (minus) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
					initFirst = new Init("*temp", Type.BOOLEAN, this.getValueBoolFirst());
					break;
					case Type.INTEGER:
					this.setTypeFirst(Type.INTEGER);
					this.setNameFirst("*temp");
					this.setValueIntegerFirst(new Integer(temp).intValue());
					//	else this.setValueIntegerFirst(-(new Integer(temp).intValue()));
					//if (not) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
					initFirst = new Init("*temp", Type.INTEGER, this.getValueIntegerFirst());
					break;
					case Type.FLOAT:
					this.setTypeFirst(Type.FLOAT);
					this.setNameFirst("*temp");
					this.setValueFloatFirst(new Float(temp).floatValue());
					//	else this.setValueFloatFirst(-(new Float(temp).floatValue()));
					//if (not) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
					initFirst = new Init("*temp", Type.FLOAT, this.getValueFloatFirst());
					break;										
					}
				}
			else if (newText.indexOf(Operator.NOT) != 0) throw new JCppBadStatementException("!!!!!!!!!!!z�a sk�adnia");
			if (newText.indexOf(operator) < newText.length() - 1)
				{
				String temp = newText.substring(newText.indexOf(operator) + operator.length(), newText.length());
				if (temp.indexOf("-") == 0)
					{
					minusSecond = true;
					temp = jCppUtil.getLost(temp, "-");
					}
				if (temp.indexOf("!") == 0)
					{
					notSecond = true;
					temp = jCppUtil.getLost(temp, "!");
					}			
				switch (jCppUtil.getType(temp))
					{
					case -1:
					this.setNameSecond(temp);
					break;
					case Type.BOOLEAN:
					this.setTypeSecond(Type.BOOLEAN);
					this.setNameSecond("*temp");
					this.setValueBoolSecond(new Boolean(temp).booleanValue());
					//	else this.setValueBoolSecond(!(new Boolean(temp).booleanValue()));
					//if (minus) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
					initSecond = new Init("*temp", Type.BOOLEAN, this.getValueBoolSecond());
					break;
					case Type.INTEGER:
					this.setTypeSecond(Type.INTEGER);
					this.setNameSecond("*temp");
					this.setValueIntegerSecond(new Integer(temp).intValue());
					//	else this.setValueIntegerSecond(-(new Integer(temp).intValue()));
					//if (not) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
					initSecond = new Init("*temp", Type.INTEGER, this.getValueIntegerSecond());
					break;
					case Type.FLOAT:
					this.setTypeSecond(Type.FLOAT);
					this.setNameSecond("*temp");
					this.setValueFloatSecond(new Float(temp).floatValue());
					//	else this.setValueFloatSecond(-(new Float(temp).floatValue()));
					//if (not) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
					initSecond = new Init("*temp", Type.FLOAT, this.getValueFloatSecond());
					break;										
					}				
				}
			else throw new JCppBadStatementException("!!!!!!!!!!!z�a sk�adnia");
			if (this.getNameFirst() == null)
				{	//tez dla int i float gdy "-"
				
				
				
				//bool
				System.out.println(this.getNameSecond() + "*********");
				if (this.getNameSecond().indexOf("*temp") == 0)
					{
					this.setValue(!(new Boolean(this.getValueBoolSecond()).booleanValue()));
					return out;
					}
				System.out.println("jestem");
				if (jCppUtil.getInitByName(this.getNameSecond(), initList, lineNumber).getType() != Type.BOOLEAN) throw new JCppBadOperatorException("!!!!!!!!!!!!!!!!!!!!!!z�y operator do danego typu");
				this.setValue(!jCppUtil.getInitByName(this.getNameSecond(), initList, lineNumber).getValueBool());
				return out;
				}
			if (initFirst == null) initFirst = jCppUtil.getInitByName(this.getNameFirst(), initList, lineNumber);
			if (initSecond == null) initSecond = jCppUtil.getInitByName(this.getNameSecond(), initList, lineNumber);							
			
			if (jCppUtil.compareInit(initFirst, initSecond) < 3)
				{
				if (initFirst != null && initSecond != null)
					{
					if (((CValueOperator)(initFirst.getClassType())).isOperatorOk(operator))
						{
						//System.out.println(operator + "yyyyyyy");
						this.setType(initFirst.getType());
						if (initFirst.getType() == Type.BOOLEAN)
							{
							boolean valueFirst = initFirst.getValueBool();
							if (jCppUtil.getType(initFirst.getName()) == -1 && notFirst) valueFirst = !valueFirst;
							if (minusFirst) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							boolean valueSecond = initSecond.getValueBool();
							if (jCppUtil.getType(initSecond.getName()) == -1 && notSecond) valueSecond = !valueSecond;
							if (minusSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							if (operator == Operator.AND_LOG) this.setValueBool(valueFirst && valueSecond);
							if (operator == Operator.OR_LOG) this.setValueBool(valueFirst || valueSecond);
							this.setValueString(String.valueOf(this.getValueBool()));
							}
						else if (initFirst.getType() == Type.INTEGER)
							{
							float valueFirst = initFirst.getValueInteger();
							if (jCppUtil.getType(initFirst.getName()) == -1 && minusFirst) valueFirst = -valueFirst;
							if (notFirst) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							float valueSecond = initSecond.getValueInteger();
							if (initSecond.getType() == Type.FLOAT) valueSecond = initSecond.getValueFloat();
							if (jCppUtil.getType(initSecond.getName()) == -1 && minusSecond) valueSecond = -valueSecond;
							if (notSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
				System.out.println("1 - " + initFirst.getValueInteger());
				System.out.println("2 - " + initSecond.getValueInteger());
							if (operator == Operator.PLUS) this.setValueInteger((int)(valueFirst + valueSecond));
							if (operator == Operator.MINUS) this.setValueInteger((int)(valueFirst - valueSecond));
							if (operator == Operator.RAZY) this.setValueInteger((int)(valueFirst * valueSecond));
							if (operator == Operator.DIV) this.setValueInteger((int)(valueFirst / valueSecond));		
							this.setValueString(String.valueOf(this.getValueInteger()));					
							}
						else 
							{
							float valueFirst = initFirst.getValueFloat();
							if (jCppUtil.getType(initFirst.getName()) == -1 && minusFirst) valueFirst = -valueFirst;
							if (notFirst) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							float valueSecond = initSecond.getValueFloat();
							if (initSecond.getType() == Type.INTEGER) valueSecond = initSecond.getValueInteger();
							if (jCppUtil.getType(initSecond.getName()) == -1 && minusSecond) valueSecond = -valueSecond;
							if (notSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							
							if (operator == Operator.PLUS) this.setValueFloat(valueFirst + valueSecond);
							if (operator == Operator.MINUS) this.setValueFloat(valueFirst - valueSecond);
							if (operator == Operator.RAZY) this.setValueFloat(valueFirst * valueSecond);
							if (operator == Operator.DIV) this.setValueFloat(valueFirst / valueSecond);	
							this.setValueString(String.valueOf(this.getValueFloat()));							
							}
						//toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
						}
					else throw new JCppBadOperatorException("!!!!!!!!!!!!!!!!!!!!!!z�y operator do type");
					}
				else throw new JCppDifferentTypesException("!!!!!!!!!!!!!!!!!!!!!!!!!!!z�y typ");
				}
			}
		else //!operator
			{
			Init init;
			was = false;
			boolean minusFirst = false, notFirst = false;
			int whatIfNo = 0;
			if (newText.equals("!true"))
				{
				this.setType(Type.BOOLEAN);
				this.setValueBoolFirst(!true);					
				was = true;	
				}
			else if (newText.equals("!false"))
				{
				this.setType(Type.BOOLEAN);
				this.setValueBoolFirst(!false);					
				was = true;	
				}				
			else 
				{
				if (jCppUtil.getType(newText) != -1)
					{ 
					this.setType(jCppUtil.getType(newText));
					//System.out.println("jestem");
					if (jCppUtil.getType(newText) == Type.BOOLEAN)
						{
						this.setValueBool(new Boolean(newText).booleanValue());
						this.setValueString(String.valueOf(this.getValueBool()));
						}
					if (jCppUtil.getType(newText) == Type.INTEGER)
						{
						this.setValueInteger(new Integer(newText).intValue());
						this.setValueString(String.valueOf(this.getValueInteger()));
						this.setValueFloat(new Float(newText).floatValue());
						}
					if (jCppUtil.getType(newText) == Type.FLOAT)
						{
						this.setValueFloat(new Float(newText).floatValue());
						this.setValueString(String.valueOf(this.getValueFloat()));
						}
					return true;
					}					
				if (newText.indexOf("!") == 0)
					{
					notFirst = true;
					newText = jCppUtil.getLost(newText, "!");
					}	
				if (newText.indexOf("-") == 0)
					{
					minusFirst = true;
					newText = jCppUtil.getLost(newText, "-");
					}									
				for (int i=0;i<initList.size();i++)
					{
					init = (Init)initList.get(i);
					if (init.getName().equals(newText))
						{
						this.setNameFirst(newText);
						if ((init.getLineFrom() < lineNumber && init.getLineTo() > lineNumber) || init.isGlobal())
							{
							if (init.getType() == Type.BOOLEAN)
								{
								this.setType(Type.BOOLEAN);
								if (init.getHasValue())
									{
									if (!notFirst) this.setValueBool(init.getValueBool());
										else this.setValueBool(!(init.getValueBool()));
									this.setValueString(String.valueOf(this.getValueBool()));
									was = true;
									}
								whatIfNo = 1;
								}
							if (init.getType() == Type.INTEGER)
								{
								this.setType(Type.INTEGER);
								if (init.getHasValue())
									{
									if (!minusFirst) this.setValueInteger(init.getValueInteger());
										else this.setValueInteger(-(init.getValueInteger()));
									this.setValueString(String.valueOf(this.getValueInteger()));
									was = true;
									}
								whatIfNo = 1;
								}	
							if (init.getType() == Type.FLOAT)
								{
								this.setType(Type.FLOAT);
								if (init.getHasValue())
									{
									if (!minusFirst) this.setValueFloat(init.getValueFloat());
										else this.setValueFloat(-(init.getValueFloat()));
									this.setValueString(String.valueOf(this.getValueFloat()));
									was = true;
									}
								whatIfNo = 1;
								}															
							whatIfNo = 2;
							}
						}
					}
				}
			if (!was)
				{
				if (whatIfNo == 0) throw new JCppNotInitException("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!nie zainicjowana zmienna lub b��d sk�adni");
				if (whatIfNo == 1) throw new JCppHasNoValueException("!!!!!!!!!!!!!!!!!!!!!!!!!!nie ma warto�ci");
				if (whatIfNo == 2) throw new JCppDifferentTypesException("!!!!!!!!!!!!!!!!!!!!!!!!!!!z�y typ");
				}
			}
		return out;				
		}
	public static void main(String[] argv)
		{
		Init jeden = new Init("jeden", Type.FLOAT, (float)10);
		jeden.setGlobal(true);
		Init dwa = new Init("dwa", Type.INTEGER, 8);
		dwa.setGlobal(true);
		List list = new java.util.ArrayList();
		list.add(jeden);
		list.add(dwa);			
		Sentence s = new Sentence(" true ", list, 1);
		System.out.println(s.getValueString());
		//System.out.println(s.getValueBool());
		//System.out.println(s.getType());
		//if (s.getType() == Type.BOOLEAN) System.out.println(s.getValueBool());
		//if (s.getType() == Type.INTEGER) System.out.println(s.getValueInteger());
		//if (s.getType() == Type.FLOAT) System.out.println(s.getValueFloat());	
		}			
	}