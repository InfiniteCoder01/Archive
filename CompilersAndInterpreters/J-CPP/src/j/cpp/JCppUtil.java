package j.cpp;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
public class JCppUtil
	{
	public final static int THE_SAME_TYPE = 0;
	public final static int INT_FLOAT_TYPE = 1;
	public final static int FLOAT_INT_TYPE = 2;
	public final static int WRONG_TYPE = 3;
	private String[] keywords;
	private String[] operators;
	public String[] getKeywords()
		{
		try
			{
			Class c = Class.forName("Keyword");
			Field[] fields = c.getFields();
			Keyword keyword = new Keyword();
			keywords = new String[fields.length];
			for (int i=0;i<fields.length;i++)
				{
				keywords[i] = (String)fields[i].get(keyword);
				}
			}
		catch (ClassNotFoundException e){System.out.println(e);}
		catch (IllegalAccessException e){System.out.println(e);}
		return keywords;
		}
	public String[] getOperators()
		{
		try
			{
			Class c = Class.forName("Operator");
			Field[] fields = c.getFields();
			Operator operator = new Operator();
			operators = new String[fields.length];
			for (int i=0;i<fields.length;i++)
				{
				operators[i] = (String)fields[i].get(operator);
				}
			}
		catch (ClassNotFoundException e){System.out.println(e);}
		catch (IllegalAccessException e){System.out.println(e);}
		return operators;
		}
	public String[] getIfOperators()
		{
		String[] ifOperators = {Operator.MNIEJ_THE_SAME,
								Operator.WIECEJ_THE_SAME,
							    Operator.MNIEJ, 
							    Operator.WIECEJ, 
							    Operator.OTHER,
							    Operator.THE_SAME,
							    Operator.AND_LOG,
							    Operator.OR_LOG,
							    Operator.NOT};
		return ifOperators;
		}
	public String[] getSentenceOperators()
		{
		String[] operators = {Operator.PLUS,
							  Operator.MINUS,
							  Operator.RAZY,
							  Operator.DIV,
							  Operator.AND_LOG,
							  Operator.OR_LOG};
		return operators;
		}
	public static String getLost(String from, String what)
		{
		while (from.indexOf(what) != -1) from = from.substring(0, from.indexOf(what)) + from.substring(from.indexOf(what) + what.length(), from.length());
		return from;
		}
	public Init getInitByName(String name, List initList, int lineNumber)
		{
		Init init = null;
		boolean was = false;
		for (int i=0;i<initList.size();i++)
			{
			if (!was) init = (Init)initList.get(i);
			if (init.getName().equals(name))
				{
				if (init.isGlobal() || (init.getLineFrom() <= lineNumber && init.getLineTo() >= lineNumber))
					{
					was = true;
					} 
				}
			}
		if (was) return init;
			else throw new JCppNotInitException("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!nie zainicjowana zmienna");		
		}
	public int compareInit(String nameFirst, String nameSecond, List initList, int lineNumber)
		{
		Init initFirst = null, initSecond = null;
		boolean wasFirst = false, wasSecond = false;
		if (nameFirst.indexOf("*temp") == 0) wasFirst = true;
		if (nameSecond.indexOf("*temp") == 0) wasSecond = true;
		if (!wasFirst)
			{
			for (int i=0;i<initList.size();i++)
				{
				if (!wasFirst) initFirst = (Init)initList.get(i);
				if (initFirst.getName().equals(nameFirst))
					{
					if (initFirst.isGlobal() || (initFirst.getLineFrom() <= lineNumber && initFirst.getLineTo() >= lineNumber))
						{
						wasFirst = true;
						} 
					}
				}
			}
		if (!wasSecond)
			{	
			for (int i=0;i<initList.size();i++)
				{
				if (!wasSecond) initSecond = (Init)initList.get(i);
				if (initSecond.getName().equals(nameSecond))
					{
					if (initSecond.isGlobal() || (initSecond.getLineFrom() <= lineNumber && initSecond.getLineTo() >= lineNumber))
						{
						wasSecond = true;
						} 
					}
				}
			}
		if (wasFirst && wasSecond) return this.compareInit(initFirst, initSecond);
			else throw new JCppNotInitException("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!nie zainicjowana zmienna");
		}
	public int getType(String text)
		{
		if (text.equals("true") || text.equals("false")) return Type.BOOLEAN;
		try
			{
			new Integer(text);
			return Type.INTEGER;
			}
		catch (NumberFormatException e){}
		try
			{
			new Float(text);
			return Type.FLOAT;
			}
		catch (NumberFormatException e){}
		return -1;		
		}
	public int compareInit(Init initFirst, Init initSecond)
		{
		if (initFirst.getType() == initSecond.getType()) return this.THE_SAME_TYPE;
		if (initFirst.getType() == Type.INTEGER && initSecond.getType() == Type.FLOAT) return this.INT_FLOAT_TYPE;
		if (initFirst.getType() == Type.FLOAT && initSecond.getType() == Type.INTEGER) return this.FLOAT_INT_TYPE;
		return this.WRONG_TYPE;
		}
	public static void sleep(int mil)
		{
		try
			{
			Thread.sleep(mil);
			}
		catch (Exception e){}
		}
	public static void main(String[] argv)
		{
		JCppUtil j = new JCppUtil();
		//String []a = j.getIfOperators();
		//for (int i=0;i<a.length;i++) System.out.println(a[i]);
		//System.out.println(j.getLost(" ffff       ff      g", " "));

		Init jeden = new Init("jeden", Type.INTEGER, 1);
		jeden.setGlobal(true);
		Init dwa = new Init("dwa", Type.BOOLEAN, false);
		dwa.setGlobal(true);
		List list = new java.util.ArrayList();
		list.add(jeden);
		list.add(dwa);
		System.out.println(j.getInitByName("dwas", list, 1));
		//System.out.println(j.compareInit("jeden", "dwa", list, 1));
		//IfCase ifCase = new IfCase("(  jeden  &&  dwa )", list, 1);
		//System.out.println(ifCase.getNameFirst());
		//System.out.println(ifCase.getOperator());
		//System.out.println(ifCase.getNameSecond());
		}
	}	