package j.cpp;

import java.util.List;
public class SetValuer
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
	public SetValuer(String newText, List newInitList, int newLineNumber)
		{
		text = newText;
		doSetValue(newText, newInitList, newLineNumber);
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
	public void doSetValue(String newText, List initList, int lineNumber)
		{
		list = initList;
		boolean out = false;
		newText = JCppUtil.getLost(newText, "(");
		newText = JCppUtil.getLost(newText, ")");
		newText = JCppUtil.getLost(newText, " ");
		newText = JCppUtil.getLost(newText, ";");
		JCppUtil jCppUtil = new JCppUtil();
		//String[] operators = jCppUtil.getSentenceOperators();
		boolean was = false;
		out = true;
		boolean minusFirst = false, minusSecond = false, notFirst = false, notSecond = false;
		Init initFirst = null;
		Init initSecond = null;
		operator = "=";
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
			if (jCppUtil.getType(temp) == -1) this.setNameFirst(temp);
				else throw new JCppBadStatementException("!!!!!!!!!!!!!!!!z�a warto�� po lewej stronie");
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
			//System.out.println("(" + temp + ")");				
			switch (jCppUtil.getType(temp))
				{
				/*case -1:
				this.setNameSecond(temp);
				break;*/
				case Type.BOOLEAN:
		System.out.println(temp + " " + jCppUtil.getType(temp));
				this.setTypeSecond(Type.BOOLEAN);
				this.setNameSecond("*temp");
				this.setValueBoolSecond(new Boolean(temp).booleanValue());
				//	else this.setValueBoolSecond(!(new Boolean(temp).booleanValue()));
				//if (minus) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
				initSecond = new Init("*temp", Type.BOOLEAN, this.getValueBoolSecond());
				break;
				case Type.INTEGER:
		System.out.println(temp + " " + jCppUtil.getType(temp));				
				this.setTypeSecond(Type.INTEGER);
				this.setNameSecond("*temp");
				this.setValueIntegerSecond(new Integer(temp).intValue());
				//	else this.setValueIntegerSecond(-(new Integer(temp).intValue()));
				//if (not) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
				initSecond = new Init("*temp", Type.INTEGER, this.getValueIntegerSecond());
				break;
				case Type.FLOAT:
		System.out.println(temp + " " + jCppUtil.getType(temp));				
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
						boolean valueSecond = initSecond.getValueBool();
						if (jCppUtil.getType(initSecond.getName()) == -1 && notSecond) valueSecond = !valueSecond;
						if (minusSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
						initFirst.setValueBool(valueSecond);
			System.out.println("bool " + initFirst.getValueBool());
						}
					else if (initFirst.getType() == Type.INTEGER)
						{
						float valueSecond = initSecond.getValueInteger();
						if (initSecond.getType() == Type.FLOAT) valueSecond = initSecond.getValueFloat();
						if (jCppUtil.getType(initSecond.getName()) == -1 && minusSecond) valueSecond = -valueSecond;
						if (notSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
						System.out.println(valueSecond + ";");
						if (valueSecond != (int)valueSecond) throw new JCppDifferentTypesException("!!!!!!!!!!z�y typ");
						initFirst.setValueInteger((int)valueSecond);
			System.out.println("integer " + initFirst.getValueInteger());						
						}
					else 
						{
						float valueSecond = initSecond.getValueFloat();
						if (initSecond.getType() == Type.INTEGER) valueSecond = initSecond.getValueInteger();
						if (jCppUtil.getType(initSecond.getName()) == -1 && minusSecond) valueSecond = -valueSecond;
						if (notSecond) throw new JCppBadOperatorException("!!!!!!!!!!!!z�y operator");
						initFirst.setValueFloat(valueSecond);
			System.out.println("float " + initFirst.getValueFloat());													
						}
					//toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
					}
				else throw new JCppBadOperatorException("!!!!!!!!!!!!!!!!!!!!!!z�y operator do type");
				}
			else throw new JCppDifferentTypesException("!!!!!!!!!!!!!!!!!!!!!!!!!!!z�y typ");
			}
		for (int i=0;i<initList.size();i++)
			{
			initSecond = (Init)initList.get(i);
			if (initSecond.getName().equals(initFirst.getName()) && initSecond.getLineFrom() == initFirst.getLineFrom() && initSecond.getLineTo() == initFirst.getLineTo())
				{
				list.set(i, initFirst);
				}
			}
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
		SetValuer s = new SetValuer("jeden =5.1", list, 1);
		System.out.println(((Init)list.get(0)).getValueFloat());
		}			
	}