package j.cpp;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
public class Code
	{
	private String code;
	private List lineList;
	private List initList;
	private String deleteCode;
	private boolean main;
	private int mainFrom, mainTo;
	private int tabs;
	private Line lastLine;
	private CCompiler cCompiler;
	private ConsoleThread consoleThread;
	private Thread thread;
	private int makeLineNumber = -1;
	private boolean ifForElse, ifForElseTrue;
	public Code(String newCode)
		{
		code = newCode;
		initList = new ArrayList();
		consoleThread = new ConsoleThread();
		thread = new Thread(consoleThread);
		}
	public static void main(String[] argv)
		{
		//System.out.println(Keyword.BOOL);
		//Line line = new Line("for(int   j=0;j<10;j++) if   \"  \" \\\"\"  <for, \"while.for.while");
		//line.doOneSpace();
		//System.out.println(line.getLine());
		Code tempCode = new Code(null);//for(int i=0;i<10;i++) +:while((osiem)) os if()iem\"{{{{}}}}{\"{*}}else dupa**{siedem;sz\n\nesc\n\naaa   +  aaaaaa}a;");
		tempCode.setCode(tempCode.getFromFile("Test5.txt"));
//while(1) while(2){k;}while(3){a;a;while(4)b;a;}		
		System.out.println(tempCode.getCode() + "\n***************\n");
		tempCode.setLineList(tempCode.doFineLines());
		tempCode.make(tempCode.getLineList());
		}
	public void setMain(boolean newMain)
		{
		main = newMain;
		}
	public void resetInitList()
		{
		initList.clear();
		}
	public boolean isMain()
		{
		return main;
		}
	public Line getLastLine()
		{
		return lastLine;
		}
	public void setLastLine(Line newLastLine)
		{
		lastLine = newLastLine;
		}
	public CCompiler getCCompiler()
		{
		return cCompiler;
		}
	public void setCCompiler(CCompiler newCCompiler)
		{
		cCompiler = newCCompiler;
		}
	public void addToInitList(Init init)
		{
		Init initTemp;
		for (int i=0;i<initList.size();i++)
			{
			initTemp = (Init)initList.get(i);
			if (initTemp.getName().equals(init.getName()) &&
				((initTemp.getLineFrom() <= init.getLineFrom() &&
				  initTemp.getLineTo()  >= init.getLineFrom()) ||
				  initTemp.isGlobal() ||
				  init.isGlobal())) throw new JCppAlreadyInitException("!!!!!! ju� zainicjowana zmienna");
			}
		initList.add(init);
		}
	public void addToListLine(Line line)
		{
		if (lineList == null) lineList = new ArrayList();
		lineList.add(line);
		}
	public boolean setInLineList(Line line, int number)
		{
		boolean out = false;
		if (lineList != null)
			{
			if (number < lineList.size() && number > -1)
				{
				lineList.set(number, line);
				out = true;
				}
			}
		return out;
		}
	public boolean removeFromLineList(int number)
		{
		boolean out = false;
		if (lineList != null)
			{
			if (number < lineList.size() && number > -1)
				{
				lineList.remove(number);
				out = true;
				}
			}
		return out;
		}
	public List getLineList()
		{
		return lineList;
		}
	public void setLineList(List newLineList)
		{
		lineList = newLineList;
		}
	private String getFromFile(String name)
		{
		String line = "";
		String out = "";
		BufferedReader bR = null;
		try
			{
			bR = new BufferedReader(new FileReader(name));
			while ((line = bR.readLine()) != null)
				{
				out += line;
				}
			bR.close();
			}
		catch (IOException e){System.out.println(e);}
		return out;
		}
	public int getLineListSize()
		{
		int out = -1;
		if (lineList != null)
			{
			out = lineList.size();
			}
		return out;
		}
	public void clearLineList()
		{
		if (lineList != null) lineList.clear();
		}
	public void setCode(String newCode)
		{
		code = newCode;
		}
	public String getCode()
		{
		return code;
		}
	private String changeGood(String where, String oldText, String newText)
		{
		BufferedReader reader = new BufferedReader(new StringReader(where));
		String temp = "";
		String line;
		int place;
		boolean ok;
		try
			{
			while ((line = reader.readLine()) != null)
				{
				ok = true;
				place = 0;	
				while (line.indexOf(oldText, place) != -1 && ok)
					{
					if (Line.isOutSide(line.substring(0, line.indexOf(oldText, place) + oldText.length()), oldText))
						{
						if (line.indexOf(oldText, place) + oldText.length() < line.length())
							{
							line = line.substring(0, line.indexOf(oldText, place)) + newText + line.substring(line.indexOf(oldText, place) + oldText.length(), line.length());
							}
						else ok = false;
						}
					else place = line.indexOf(oldText, place) + oldText.length();
					}
				temp += line;				
				}
			}
		catch (IOException e){}		
		return temp;
		}
	private int getIndexOfOperator(String text, int place, String operator, String operatorBack)
		{
		int number = 1;
		int newPlace = -1;
		while (number > 0)
			{
			place++;
			if (place > text.length()) throw new JCppBadStatementException("!!!!!!!!!!!!!poza funkcj� main() lub brak klamry ko�cz�cej main()");;
			if (text.indexOf(operator, place) == place) number++;
			if (text.indexOf(operatorBack, place) == place) number--;
			if (number == 0) newPlace = place;
			}
		if (place == -1) throw new JCppBadStatementException("!!!!!!!!!!!!!nie ma " + operatorBack + " konczacego");
		return newPlace;
		}
	private String insert(String text, String what, int where)
		{
		String out = "";
		out = text.substring(0, where + 1) + what + text.substring(where + 1, text.length()).trim();
		return out;
		}
	private int getHowManyOperators(String text, String what)
		{
		int out = 0;
		for (int i=0;i<text.length();i++)
			{
			if (text.indexOf(what, i) == i) out++;
			}
		return out;
		}
	private String setEndLine(String text)
		{
		//System.out.println("^^" + this.insert(code, "?", text.indexOf(";")) + "^^");
		for (int i=0;i<text.length();i++)
			{
			if (text.indexOf(";", i) == i && Line.isOutSide(text.substring(0, i + 1), ";") && Line.isOutSideFor(text.substring(0, i + 1), ";"))
				{
				text = this.insert(text, "\n", i);
				}
			if (text.indexOf("{", i) == i && Line.isOutSide(text.substring(0, i + 1), "{"))
				{
				text = this.insert(text, "\n", i);
				text = this.insert(text, "\n", i - 1);
				i++;
				}
			if (text.indexOf("}", i) == i && Line.isOutSide(text.substring(0, i + 1), "}"))
				{
				text = this.insert(text, "\n", i);
				}	
											
			}
		return text;
		}
	public List doFineLines()	//ale bedzie List
		{
		List list = new ArrayList();
		Line lineOne = null;
		code = this.changeGood(code, Operator.TAB, Operator.SPACE);
		code = this.changeGood(code, "\n", "");
		Line line = new Line(code);
		line.doOneSpace();
		code = line.getLine();
		System.out.println(code);	
		code = this.setEndLine(code);
		System.out.println("&&&&&&&&&&");
		System.out.println(code);
		List blockList = new ArrayList();
		for (int i=0;i<code.length();i++)
			{
			if (code.indexOf(Keyword.MAIN, i) == i && Line.isOutSide(code, Keyword.MAIN))
				{
				int whereStart = code.indexOf("{", i);
				if (whereStart == -1) throw new JCppBadStatementException("!!!!!!!!brak operatora: " + Operator.KLAMRA_LEFT);
				blockList.add(new Block(code.substring(i, whereStart).trim(),
										Block.MAIN,
										whereStart + 1, 
										this.getIndexOfOperator(code, whereStart, "{", "}")));
				if (main) throw new JCppTooMuchMainException("!!!!!!!!!!za duzo main");
				main = true;
				}
			if (code.indexOf(Keyword.IF, i) == i && Line.isOutSide(code, Keyword.IF))
				{
				int whereStart = code.indexOf("{", i);
				if (whereStart == -1) throw new JCppBadStatementException("!!!!!!!!brak operatora: " + Operator.KLAMRA_LEFT);
				blockList.add(new Block(code.substring(i, whereStart).trim(),
										Block.IF,
										whereStart + 1, 
										this.getIndexOfOperator(code, whereStart, "{", "}")));
				}
			if (code.indexOf(Keyword.WHILE, i) == i && Line.isOutSide(code, Keyword.WHILE))
				{
				int whereStart = code.indexOf("{", i);
				if (whereStart == -1) throw new JCppBadStatementException("!!!!!!!!brak operatora: " + Operator.KLAMRA_LEFT);
				blockList.add(new Block(code.substring(i, whereStart).trim(),
										Block.WHILE,
										whereStart + 1, 
										this.getIndexOfOperator(code, whereStart, "{", "}")));
				}
			if (code.indexOf(Keyword.FOR, i) == i && Line.isOutSide(code, Keyword.FOR))
				{
				int whereStart = code.indexOf("{", i);
				if (whereStart == -1) throw new JCppBadStatementException("!!!!!!!!brak operatora: " + Operator.KLAMRA_LEFT);
				blockList.add(new Block(code.substring(i, whereStart).trim(),
										Block.FOR,
										whereStart + 1, 
										this.getIndexOfOperator(code, whereStart, "{", "}")));
				}												
			}
		//System.out.println(code.substring(
		try
			{
			List masterLine = new ArrayList();	//kolejka line do blok�w
			Line lineTemp;
			BufferedReader bR = new BufferedReader(new StringReader(code));
			String stringLine;
			String stringTemp = "";
			int lineNumber = 0;
			while ((stringLine = bR.readLine()) != null)
				{
//System.out.println(stringLine);
//JCppUtil.sleep(500);
				lineOne = new Line(stringLine);
				lineOne.setLineNumber(lineNumber);
				lineNumber++;
				lineOne.setHowManyEnds(this.getHowManyOperators(stringTemp, Operator.END_LINE));
				lineOne.setHowManyStartStop(this.getHowManyOperators(stringTemp, Operator.KLAMRA_LEFT) + 
											this.getHowManyOperators(stringTemp, Operator.KLAMRA_RIGHT));
				lastLine = lineOne;
				System.out.println("lastLine");
				stringTemp += stringLine;				
				if (!this.isBlockOperator(stringLine))
					{
					if (masterLine.size() == 0) list.add(lineOne);
						else
							{
							lineTemp = (Line)masterLine.get(masterLine.size() - 1);
							lineTemp.addToSmallLineList(lineOne);
		System.out.println(lineTemp.getLine() + " ---> " + lineOne.getLine());
							masterLine.set(masterLine.size() - 1, lineTemp);
							}
					}
				if (this.isBlock(stringLine)) masterLine.add(lineOne);
					else if (this.isBlockEnd(stringLine))
						{
						if (masterLine.size() == 0) throw new JCppBadStatementException("!!!!!!!!!! " + Operator.KLAMRA_RIGHT + " bez bloku");
						if (masterLine.size() == 1)
							{
							boolean canInsert = true;
							for (int i=0;i<list.size();i++)
								{
								if (((Line)list.get(i)).equals((Line)masterLine.get(0))) canInsert = false;
								}
							if (canInsert) list.add((Line)masterLine.get(0));
							}
						masterLine.remove(masterLine.size() - 1);
						}								
				}
			}
		catch (IOException e){}
		System.out.println("$$$$$$$$$$$$$$$$");
		this.checkLines(list);
		//for (int i=0;i<list.size();i++) System.out.println((Line)list.get(i));
		return list;
		}
	private int getLineOfOperator(String code, int from)
		{
		System.out.println("FROM " + from);
		int place = -1;
		int endCount = 0;
		for (int i=0;i<code.length();i++)
			{
			if (code.indexOf("\n", i) == i) endCount++;
			if (place == -1 && endCount > from) place = i;
			}
		int temp = this.getIndexOfOperator(code, place, "{", "}");
		System.out.println(temp);
		int to = 0;
		for (int i=0;i<temp;i++)
			{
			if (code.indexOf("\n", i) == i) to++;
			}
		return to;
		}
	private String findSentence(String temp, Line line)
		{
		Sentence sentence;
		Init init;
		SetValuer setValuer;
		COut cOut;
		IfCase ifCase;
		int place = -1;
		while (place < temp.length())
			{
			place++;
			if (temp.indexOf(Operator.NAWIAS_LEFT, place) == place && 
				temp.indexOf(Operator.NAWIAS_RIGHT, place) != -1)
				{
				String bigTemp = temp.substring(temp.indexOf(Operator.NAWIAS_LEFT, place) + 1, 
					temp.indexOf(Operator.NAWIAS_RIGHT, place));
				if (temp.substring(temp.indexOf(Operator.NAWIAS_LEFT, place) + 1, 
					temp.indexOf(Operator.NAWIAS_RIGHT, place)).indexOf(Operator.NAWIAS_LEFT) == -1 &&
					bigTemp.length() > 2 &&
					bigTemp.indexOf(";") == -1 &&
					bigTemp.indexOf(Operator.MNIEJ_THE_SAME) == -1 &&
					bigTemp.indexOf(Operator.WIECEJ_THE_SAME) == -1 &&
					bigTemp.indexOf(Operator.MNIEJ) == -1 &&
					bigTemp.indexOf(Operator.WIECEJ) == -1 &&
					bigTemp.indexOf(Operator.OTHER) == -1 &&
					bigTemp.indexOf(Operator.THE_SAME) == -1 &&
					bigTemp.indexOf(Operator.AND_LOG) == -1 &&
					bigTemp.indexOf(Operator.OR_LOG) == -1 &&
					bigTemp.indexOf(Operator.NOT) == -1
					)
					{
					System.out.println(temp.substring(temp.indexOf(Operator.NAWIAS_LEFT, place) + 1, 
					temp.indexOf(Operator.NAWIAS_RIGHT, place)) + "______");						
					if (Line.isOutSide(temp.substring(0, place + 1), Operator.NAWIAS_LEFT) &&
						Line.isOutSide(temp.substring(0, temp.indexOf(Operator.NAWIAS_RIGHT, place) + 1), Operator.NAWIAS_RIGHT))
						{
						String tempSmall = temp.substring(place + 1, temp.indexOf(Operator.NAWIAS_RIGHT, place));
						System.out.println(temp);
						System.out.println(tempSmall + "~~~~~~~~~()");
						sentence = new Sentence(tempSmall, initList, line.getLineNumber());
						temp = temp.substring(0, place) + sentence.getValueString() + temp.substring(temp.indexOf(Operator.NAWIAS_RIGHT, place) + 1, temp.length());
						place = -1;
						}
					}
				}
			}
		return temp;			
		}
	private String findArraySentence(String temp, Line line)
		{
		Sentence sentence;
		Init init;
		SetValuer setValuer;
		COut cOut;
		IfCase ifCase;
		int place = 0;
		while (place < temp.length())
			{
			if (temp.indexOf(Operator.KWADRAT_LEFT, place) == place && 
				temp.indexOf(Operator.KWARDAT_RIGHT, place) != -1)
				{
				if (Line.isOutSide(temp.substring(0, place + 1), Operator.KWADRAT_LEFT) &&
					Line.isOutSide(temp.substring(0, temp.indexOf(Operator.KWARDAT_RIGHT, place) + 1), Operator.KWARDAT_RIGHT))
					{
					String tempSmall = temp.substring(place + 1, temp.indexOf(Operator.KWARDAT_RIGHT, place));
					System.out.println(tempSmall + "~~~~~~~~~");
					sentence = new Sentence(tempSmall, initList, line.getLineNumber());
					temp = temp.substring(0, place + 1) + sentence.getValueString() + temp.substring(temp.indexOf(Operator.KWARDAT_RIGHT, place), temp.length());
					}
				}
			place++;
			}
		return temp;			
		}
	private String changeIfs(String temp, Line line)
		{
		Sentence sentence;
		Init init;
		SetValuer setValuer;
		COut cOut;
		IfCase ifCase;
		int place = -1;
		while (place < temp.length())
			{
			place++;
			if (temp.indexOf(Operator.NAWIAS_LEFT, place) == place && 
				temp.indexOf(Operator.NAWIAS_RIGHT, place) != -1)
				{
				String bigTemp = temp.substring(temp.indexOf(Operator.NAWIAS_LEFT, place) + 1, 
					temp.indexOf(Operator.NAWIAS_RIGHT, place));
				if (temp.substring(temp.indexOf(Operator.NAWIAS_LEFT, place) + 1, 
					temp.indexOf(Operator.NAWIAS_RIGHT, place)).indexOf(Operator.NAWIAS_LEFT) == -1 &&
					bigTemp.length() > 2 &&
					bigTemp.indexOf(";") == -1 &&
					(
					bigTemp.indexOf(Operator.MNIEJ_THE_SAME) != -1 ||
					bigTemp.indexOf(Operator.WIECEJ_THE_SAME) != -1 ||
					bigTemp.indexOf(Operator.MNIEJ) != -1 ||
					bigTemp.indexOf(Operator.WIECEJ) != -1 ||
					bigTemp.indexOf(Operator.OTHER) != -1 ||
					bigTemp.indexOf(Operator.THE_SAME) != -1 ||
					bigTemp.indexOf(Operator.AND_LOG) != -1 ||
					bigTemp.indexOf(Operator.OR_LOG) != -1 ||
					bigTemp.indexOf(Operator.NOT) != -1
					))
					{
					System.out.println(temp.substring(temp.indexOf(Operator.NAWIAS_LEFT, place) + 1, 
					temp.indexOf(Operator.NAWIAS_RIGHT, place)) + "______");						
					if (Line.isOutSide(temp.substring(0, place + 1), Operator.NAWIAS_LEFT) &&
						Line.isOutSide(temp.substring(0, temp.indexOf(Operator.NAWIAS_RIGHT, place) + 1), Operator.NAWIAS_RIGHT))
						{
						String tempSmall = temp.substring(place + 1, temp.indexOf(Operator.NAWIAS_RIGHT, place));
						System.out.println(temp);
						System.out.println(tempSmall + "~~~~~~~~~()");
						ifCase = new IfCase(tempSmall, initList, line.getLineNumber());
						//sentence = new Sentence(tempSmall, initList, line.getLineNumber());
						temp = temp.substring(0, place) + String.valueOf(ifCase.getValue()) + temp.substring(temp.indexOf(Operator.NAWIAS_RIGHT, place) + 1, temp.length());
						place = -1;
						}
					}
				}
			}
		return temp;			
		}
	public void make(List lines)
		{
		Line lineBack = null;
		for (int i=0;i<lines.size();i++)
			{
			if (i > 0) lineBack = (Line)lines.get(i - 1);
			this.make((Line)lines.get(i), lineBack);
			}
		}
	public void make(Line line, Line lineBack)
		{
		//if (this.getCCompiler() != null && this.getCCompiler().isDebug()) if (this.getCCompiler() != null && initList.size() > 0) this.getCCompiler().setInitList(initList);
		//if (this.getCCompiler() != null && this.getCCompiler().getSleep() > 0) JCppUtil.sleep(this.getCCompiler().getSleep());
		//System.out.println("lineNumber " + line.getLineNumber());
		lastLine = line;
		Sentence sentence;
		Init init;
		SetValuer setValuer;
		COut cOut;
		IfCase ifCase;
		Line lineBackTemp = null;
		String temp = line.getLine().trim();
		//System.out.println("%%%%%%%%%%%%%%% " + temp + "%%%%%%%");
		//if (temp.indexOf("if ") == 0) ifForElse = true;
		//	else if (!(temp.indexOf("else ") == 0)) ifForElse = false;
	/*	if (temp.indexOf(Operator.KWADRAT_LEFT) > 0 && temp.indexOf(Operator.KWARDAT_RIGHT, temp.indexOf(Operator.KWADRAT_LEFT)) > 0)
			{
			String tempSmall = temp.substring(temp.indexOf(Operator.KWADRAT_LEFT) + 1, temp.indexOf(Operator.KWARDAT_RIGHT, temp.indexOf(Operator.KWADRAT_LEFT)));
			//System.out.println(tempSmall + "{{{{{{{{{{{");
			sentence = new Sentence(tempSmall, initList, line.getLineNumber());
			temp = temp.substring(0, temp.indexOf(Operator.KWADRAT_LEFT) + 1) + sentence.getValueString() + temp.substring(temp.indexOf(Operator.KWARDAT_RIGHT, temp.indexOf(Operator.KWADRAT_LEFT)), temp.length());
			//System.out.println(temp + "----------------------");
			
			}*/
		if (temp.indexOf("main ") == 0)
			{
			if (!temp.equals("main ()")) throw new JCppBadStatementException("!!!!!!!z�a sk�adnia");
			mainFrom = line.getLineNumber();
			mainTo = this.getLineOfOperator(code, mainFrom + 2) - 1;
		//	System.out.println(mainFrom + "{}{}{}{}{}{}" + mainTo);
		//	JCppUtil.sleep(5000);
			for (int i=0;i<line.getSmallLineList().size();i++)
				{
				if (i > 0) lineBackTemp = ((Line)line.getSmallLineList().get(i - 1));
				this.make(((Line)line.getSmallLineList().get(i)), lineBackTemp);
				}
			main = true;
			}
		else if (temp.indexOf("if ") == 0)
			{
			this.checkIfMain(line.getLineNumber());
			temp = this.findSentence(temp, line);
			temp = this.findArraySentence(temp, line);			
			temp = this.findSentence(temp, line);
			temp = this.changeIfs(temp, line);
			//System.out.println(temp.substring(2, temp.length()) + ":::::::::::::::::::");
			ifCase = new IfCase(temp.substring(2, temp.length()), initList, line.getLineNumber());
			//System.out.println(ifCase.getValue());
			if (ifCase.getValue())
				{
				ifForElseTrue = true;
				for (int i=0;i<line.getSmallLineList().size();i++)
					{
					if (i > 0) lineBackTemp = ((Line)line.getSmallLineList().get(i - 1));
					this.make(((Line)line.getSmallLineList().get(i)), lineBackTemp);
					}
				}
			else ifForElseTrue = false;
			}
		else if (temp.equals("else"))
			{
			ifForElse = false;
			if (lineBack != null && lineBack.getLine().indexOf("if ") == 0) ifForElse = true;
			if (!ifForElse) throw new JCppBadStatementException("!!!!!!!!! else w niew�a�ciwym miejscu");
			if (!ifForElseTrue)
				{
				this.checkIfMain(line.getLineNumber());
				for (int i=0;i<line.getSmallLineList().size();i++)
					{
					if (i > 0) lineBackTemp = ((Line)line.getSmallLineList().get(i - 1));
					this.make(((Line)line.getSmallLineList().get(i)), lineBackTemp);
					}
				}
			}
		else if (temp.indexOf("while ") == 0)
			{
			this.checkIfMain(line.getLineNumber());
			String temp1;
			//System.out.println(temp.substring(5, temp.length()) + ":::::::::::::::::::");
			temp1 = this.findSentence(temp, line);
			temp1 = this.findArraySentence(temp1, line);			
			temp1 = this.findSentence(temp1, line);
			temp1 = this.changeIfs(temp1, line);			
			ifCase = new IfCase(temp1.substring(5, temp1.length()), initList, line.getLineNumber());
			//System.out.println(ifCase.getValue());
			while (ifCase.getValue())
				{
				for (int i=0;i<line.getSmallLineList().size();i++)
					{
					if (i > 0) lineBackTemp = ((Line)line.getSmallLineList().get(i - 1));
					this.make(((Line)line.getSmallLineList().get(i)), lineBackTemp);
					}
				temp1 = this.findSentence(temp, line);
				temp1 = this.findArraySentence(temp1, line);					
				temp1 = this.findSentence(temp1, line);
				temp1 = this.changeIfs(temp1, line);						
				ifCase = new IfCase(temp1.substring(5, temp1.length()), initList, line.getLineNumber());					
				}
			}
		else if (temp.indexOf("for ") == 0)
			{
			this.checkIfMain(line.getLineNumber());
			String for1, for2, for3;
			String temp1;
			temp1 = this.findSentence(temp, line);
			temp1 = this.findArraySentence(temp1, line);			
			temp1 = this.findSentence(temp1, line);
			temp1 = this.changeIfs(temp1, line);			
			try
				{
				for1 = JCppUtil.getLost(temp.substring(3, temp.indexOf(";") + 1), "(").trim();
				for2 = JCppUtil.getLost(temp.substring(temp.indexOf(";"), temp.indexOf(";", temp.indexOf(";") + 1)), ";");
				for3 = JCppUtil.getLost(temp.substring(temp.indexOf(";", temp.indexOf(";") + 1) + 1, temp.length()), ")").trim() + ";";
				//System.out.println("<<<<<" + for1);
				//System.out.println("<<<<<" + for2);
				//System.out.println("<<<<<" + for3);
				}
			catch (Exception e){throw new JCppBadStatementException("!!!!!!!z�a sk�adnia");}
			if (for1.indexOf(Keyword.BOOL + " ") == 0 ||
				for1.indexOf(Keyword.INTEGER + " ") == 0 ||
				for1.indexOf(Keyword.FLOAT + " ") == 0)
				{
				if (for1.indexOf(Operator.TOTAL) != -1 && Line.isOutSide(for1, Operator.TOTAL))
					{
					//System.out.println(":::" + temp.substring(temp.indexOf(Operator.TOTAL) + 1, temp.length()) + ":::");
					sentence = new Sentence(for1.substring(for1.indexOf(Operator.TOTAL) + 1, for1.length()), initList, line.getLineNumber());
					for1 = for1.substring(0, for1.indexOf(Operator.TOTAL) + 1) + sentence.getValueString() + ";";
					}			
				init = new Init();
				init = init.doInit(for1, 0, 10, initList);	//!!!!!!!!!!!!
				init.setGlobal(true);	//!!!!!!!!!!!!!!!!
				this.addToInitList(init);
				//System.out.println("&&&&&&&& " + init.getName() + " " + init.getType() + " " + init.getValueString());
				}
			else if (for1.indexOf(Operator.TOTAL) != -1 && Line.isOutSide(for1, Operator.TOTAL))
				{
				sentence = new Sentence(for1.substring(for1.indexOf(Operator.TOTAL) + 1, for1.length()), initList, line.getLineNumber());
				for1 = for1.substring(0, for1.indexOf(Operator.TOTAL) + 1) + sentence.getValueString() + ";";
				setValuer = new SetValuer(for1, initList, line.getLineNumber());
				}			
			

			ifCase = new IfCase(for2, initList, line.getLineNumber());
			//System.out.println(ifCase.getValue());
			while (ifCase.getValue())
				{
				for (int i=0;i<line.getSmallLineList().size();i++)
					{
					if (i > 0) lineBackTemp = ((Line)line.getSmallLineList().get(i - 1));
					this.make(((Line)line.getSmallLineList().get(i)), lineBackTemp);
					}
				if ((for3.indexOf("++") != -1 && Line.isOutSide(for3, "++")) ||
					 (for3.indexOf("--") != -1 && Line.isOutSide(for3, "--")))
					 {
					 sentence = new Sentence(for3, initList, line.getLineNumber(), true);
					 }
				else if (for3.indexOf(Operator.TOTAL) != -1 && Line.isOutSide(temp, Operator.TOTAL))
					{
					sentence = new Sentence(for3.substring(for3.indexOf(Operator.TOTAL) + 1, for3.length()), initList, line.getLineNumber());
					//for3 = for3.substring(0, for3.indexOf(Operator.TOTAL) + 1) + sentence.getValueString() + ";";
					setValuer = new SetValuer(for3.substring(0, for3.indexOf(Operator.TOTAL) + 1) + sentence.getValueString() + ";", initList, line.getLineNumber());
					}					 
				temp1 = this.findSentence(for2, line);
				temp1 = this.findArraySentence(temp1, line);					 
				temp1 = this.findSentence(temp1, line);
				temp1 = this.changeIfs(temp1, line);					 
					 					
				ifCase = new IfCase(temp1, initList, line.getLineNumber());					
				}			
			
			
			
			}			
		//for
		//while
		//if
		else if (temp.indexOf("cout ") == 0)
			{
			this.checkIfMain(line.getLineNumber());
			temp = this.findSentence(temp, line);
			temp = this.findArraySentence(temp, line);
			temp = this.findSentence(temp, line);
			temp = this.changeIfs(temp, line);			
			cOut = new COut(temp, initList, line.getLineNumber());
			for (int i=0;i<cOut.getOut().length;i++)
				{
				System.out.print("OUT===" + cOut.getOut()[i]);
				this.consoleOut(cOut.getOut()[i]);
				}
			//!!!!!!!!!!!!!!!!!!!!!!!!!! jaki� out
			}
		else if ((temp.indexOf("++") != -1 && Line.isOutSide(temp, "++")) ||
				 (temp.indexOf("--") != -1 && Line.isOutSide(temp, "--")))
				 {
				 this.checkIfMain(line.getLineNumber());
				 sentence = new Sentence(temp.substring(temp.indexOf(Operator.TOTAL) + 1, temp.length()), initList, line.getLineNumber(), true);
				 }
		//cin
		//!!!!!!!!!!!!!!!!!!!!!!!!!jak petla albo cos to this.make(subLine)
		else if (temp.indexOf(Keyword.BOOL + " ") == 0 ||
			temp.indexOf(Keyword.INTEGER + " ") == 0 ||
			temp.indexOf(Keyword.FLOAT + " ") == 0)
			{
			temp = this.findSentence(temp, line);
			temp = this.findArraySentence(temp, line);
			temp = this.findSentence(temp, line);
			temp = this.changeIfs(temp, line);				
			int from = line.getLineNumber();
			int to = this.getLineOfOperator(code, from);
			System.out.println("TO_" + to);
			this.checkIfMain(line.getLineNumber());
			if (temp.indexOf("[") > 0 && temp.indexOf("]", temp.indexOf("[")) > 0)
				{
				init = new Init();
				Init[] inits = init.doInitArray(temp, from, to, initList);
				for (int i=0;i<inits.length;i++)
					{
					//inits[i].setGlobal(true);
					this.addToInitList(inits[i]);
					}				
				}
			else
				{
				if (temp.indexOf(Operator.TOTAL) != -1 && Line.isOutSide(temp, Operator.TOTAL))
					{
					//System.out.println(":::" + temp.substring(temp.indexOf(Operator.TOTAL) + 1, temp.length()) + ":::");
					sentence = new Sentence(temp.substring(temp.indexOf(Operator.TOTAL) + 1, temp.length()), initList, line.getLineNumber());
					temp = temp.substring(0, temp.indexOf(Operator.TOTAL) + 1) + sentence.getValueString() + ";";
					}			
				init = new Init();
				init = init.doInit(temp, from, to, initList);	//!!!!!!!!!!!!
				//init.setGlobal(true);	//!!!!!!!!!!!!!!!!
				this.addToInitList(init);
				System.out.println("&&&&&&&& " + init.getName() + " " + init.getType() + " " + init.getValueString());
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + from + " " + to);
				}
			}
		else if (temp.indexOf(Operator.TOTAL) != -1 && Line.isOutSide(temp, Operator.TOTAL))
			{
			temp = this.findSentence(temp, line);
			temp = this.findArraySentence(temp, line);
			temp = this.findSentence(temp, line);
			temp = this.changeIfs(temp, line);				
			this.checkIfMain(line.getLineNumber());
			sentence = new Sentence(temp.substring(temp.indexOf(Operator.TOTAL) + 1, temp.length()), initList, line.getLineNumber());
			temp = temp.substring(0, temp.indexOf(Operator.TOTAL) + 1) + sentence.getValueString() + ";";
			setValuer = new SetValuer(temp, initList, line.getLineNumber());
			}
		else throw new JCppBadStatementException("!!!!!!!!!!nie znana instrukcja " + temp + "!");
		}
	private void consoleOut(String text)
		{
		if (this.getCCompiler() != null) this.getCCompiler().consolePrint(text);
		}
	private String consoleIn()
		{
		String in = "";
		in = this.getCCompiler().consoleIn();
		return in;
		}
	private void checkIfMain(int lineNumber)
		{
		if (!main || lineNumber < mainFrom || lineNumber > mainTo) throw new JCppBadStatementException("!!!!!!poza funkcj� main");
		}
	private boolean isBlock(String line)
		{
		line = line.trim();
		if (line.indexOf("main ") == 0 ||
			line.indexOf("if ") == 0 ||
			line.indexOf("while ") == 0 ||
			line.indexOf("for ") == 0 ||
			line.equals("else")) return true;
		return false;
		}
	private boolean isBlockEnd(String line)
		{
		if (line.indexOf("}") == 0) return true;
		return false;
		}
	private boolean isBlockOperator(String line)
		{
		if (line.indexOf("{") == 0 || line.indexOf("}") == 0) return true;
		return false;
		}
	private void checkLines(List list)
		{
		Line line;
		for (int i=0;i<list.size();i++)
			{
			line = (Line)list.get(i);
			for (int j=0;j<tabs;j++) System.out.print("\t");
			System.out.println(line.getLine());
			if (line.getSmallLineList() != null)
				{
				tabs++;
				this.checkLines(line.getSmallLineList());
				tabs--;
				}
			}
		}
	public String getDeleteCode()
		{
		return deleteCode;
		}
	class ConsoleThread implements Runnable
		{
		public void run()
			{
			
			/*while (getCCompiler().getConsoleBlock())
				{
				try
					{
					System.out.println("wait");
					Thread.sleep(100);
					}
				catch (Exception e){}
				}*/
			}
		}
	}
