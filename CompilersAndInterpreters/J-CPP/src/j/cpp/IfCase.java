package j.cpp;

import java.util.List;
public class IfCase
	{
	private int typeFirst;
	private int typeSecond;
	private String operator;
	private boolean value;
	private boolean valueBoolFirst;
	private int valueIntegerFirst;
	private float valueFloatFirst;
	private boolean valueBoolSecond;
	private int valueIntegerSecond;
	private float valueFloatSecond;
	private String text;
	private String nameFirst;
	private String nameSecond;
	public IfCase(String newText, List newInitList, int newLineNumber)
		{
		text = newText;
		doIf(newText, newInitList, newLineNumber);
		}	
	public int getTypeFirst()
		{
		return typeFirst;
		}
	public void setTypeFirst(int newTypeFirst)
		{
		typeFirst = newTypeFirst;
		}
	public int getTypeSecondt()
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
	// ( x )
	public void doIf(String newText, List initList, int lineNumber)
		{
		newText = JCppUtil.getLost(newText, "(");
		newText = JCppUtil.getLost(newText, ")");
		newText = JCppUtil.getLost(newText, " ");
		if (newText.equals("true"))
			{
			this.setValue(true);
			return;
			}
		if (newText.equals("false"))
			{
			this.setValue(false);
			return;
			}
		JCppUtil jCppUtil = new JCppUtil();
		String[] operators = jCppUtil.getIfOperators();
		boolean was = false;
		for (int i=0;i<operators.length;i++)
			{
			if (newText.indexOf(operators[i], 1) != -1)
				{
				operator = operators[i];
				was = true;
				break;
				}
			}
		if (was)	//operator
			{
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
					return;
					}
				System.out.println("jestem");
				if (jCppUtil.getInitByName(this.getNameSecond(), initList, lineNumber).getType() != Type.BOOLEAN) throw new JCppBadOperatorException("!!!!!!!!!!!!!!!!!!!!!!z�y operator do danego typu");
				this.setValue(!jCppUtil.getInitByName(this.getNameSecond(), initList, lineNumber).getValueBool());
				return;
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
						if (initFirst.getType() == Type.BOOLEAN)
							{
							boolean valueFirst = initFirst.getValueBool();
							if (jCppUtil.getType(initFirst.getName()) == -1 && notFirst) valueFirst = !valueFirst;
							if (minusFirst) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							boolean valueSecond = initSecond.getValueBool();
							if (jCppUtil.getType(initSecond.getName()) == -1 && notSecond) valueSecond = !valueSecond;
							if (minusSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							
							if (operator == Operator.AND_LOG) this.setValue(valueFirst && valueSecond);
							if (operator == Operator.OR_LOG) this.setValue(valueFirst || valueSecond);
							if (operator == Operator.THE_SAME) this.setValue(valueFirst == valueSecond);
							if (operator == Operator.OTHER)	this.setValue(valueFirst != valueSecond);																											
							}
						else if (initFirst.getType() == Type.INTEGER)
							{
							int valueFirst = initFirst.getValueInteger();
							if (jCppUtil.getType(initFirst.getName()) == -1 && minusFirst) valueFirst = -valueFirst;
							if (notFirst) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							int valueSecond = initSecond.getValueInteger();
							if (initSecond.getType() == Type.FLOAT) valueSecond = (int)initSecond.getValueFloat();
							if (jCppUtil.getType(initSecond.getName()) == -1 && minusSecond) valueSecond = -valueSecond;
							if (notSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
							
							if (operator == Operator.MNIEJ) this.setValue(valueFirst < valueSecond);
							if (operator == Operator.WIECEJ) this.setValue(valueFirst > valueSecond);
							if (operator == Operator.MNIEJ_THE_SAME) this.setValue(valueFirst <= valueSecond);
							if (operator == Operator.WIECEJ_THE_SAME) this.setValue(valueFirst >= valueSecond);							
							if (operator == Operator.OTHER) this.setValue(valueFirst != valueSecond);
							if (operator == Operator.THE_SAME) this.setValue(valueFirst == valueSecond);
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
							
							if (operator == Operator.MNIEJ) this.setValue(valueFirst < valueSecond);
							if (operator == Operator.WIECEJ) this.setValue(valueFirst > valueSecond);
							if (operator == Operator.MNIEJ_THE_SAME) this.setValue(valueFirst <= valueSecond);
							if (operator == Operator.WIECEJ_THE_SAME) this.setValue(valueFirst >= valueSecond);							
							if (operator == Operator.OTHER) this.setValue(valueFirst != valueSecond);
							if (operator == Operator.THE_SAME) this.setValue(valueFirst == valueSecond);							
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
			boolean notFirst = false;
			int whatIfNo = 0;
			if (newText.equals("!true"))
				{
				this.setValue(!true);
				this.setValueBoolFirst(!true);					
				was = true;	
				}
			else if (newText.equals("!false"))
				{
				this.setValue(!false);
				this.setValueBoolFirst(!false);					
				was = true;	
				}
			else 
				{
				if (newText.indexOf("!") == 0)
					{
					notFirst = true;
					newText = jCppUtil.getLost(newText, "!");
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
								if (init.getHasValue())
									{
									if (!notFirst) this.setValue(init.getValueBool());
										else this.setValue(!(init.getValueBool()));
									this.setValueBoolFirst(this.getValue());
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
				if (whatIfNo == 0) throw new JCppNotInitException("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!nie zainicjowana zmienna");
				if (whatIfNo == 1) throw new JCppHasNoValueException("!!!!!!!!!!!!!!!!!!!!!!!!!!nie ma warto�ci");
				if (whatIfNo == 2) throw new JCppDifferentTypesException("!!!!!!!!!!!!!!!!!!!!!!!!!!!z�y typ");
				}
			}
		}
	public static void main(String[] argv)
		{
		Init jeden = new Init("jeden", Type.BOOLEAN, false);
		jeden.setGlobal(true);
		Init dwa = new Init("dwa", Type.INTEGER, 10);
		dwa.setGlobal(true);
		List list = new java.util.ArrayList();
		list.add(jeden);
		list.add(dwa);
		IfCase ifCase = new IfCase("( !jeden )", list, 1);
		System.out.println(ifCase.getNameFirst());
		System.out.println(ifCase.getOperator());
		System.out.println(ifCase.getNameSecond());
		System.out.println(ifCase.getValue());
		}
	}