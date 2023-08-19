package j.cpp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
public class CCompiler
	{
	private JPanel panelCenter;
	private JPanel panelTree;
	private JPanel panelDebug;
	private JPanel panelDown;
	private JPanel panelLeft;
	private JPanel panelUp;
	private JTextArea codeArea;
	private JTextArea messageArea;
	private JFrame frame;
	private JMenu fileMenu;
	private JMenuBar menuBar;
	private JMenuItem openFileMenu;
	private JMenuItem saveFileMenu;
	private JScrollPane centerPane, debugPane;
	private JFileChooser fileChooser;
	private ExampleFileFilter filter;
	private JSplitPane splitPane;
	private Toolkit toolkit;
	private JTabbedPane jT;	
	private JTree jTree;
	//private Document doc;
	//private String[] styles;
	private JButton buttonUpCompile;
	private int lineNumber;
	private	String[] keywords = null;
	private	String[] operators = null;
	private BufferedReader textReader;
	private Code code;
	private java.util.List forTreeList;
	//private JTextArea debugTextArea;
	private JPanel debugPanelInside;
	private JButton buttonShowConsole;
	
	private JFrame frameConsole;
	private JScrollPane consoleScroll;
	private JTextArea consoleArea;
	private JTextField consoleField;
	private JPanel consolePanelUp;
	private JButton consoleButtonStart, consoleButtonPause;
	private JScrollBar consoleScrollBar;
	private JLabel consoleLabel;
	private JCheckBox consoleCheck;
	
	private int errorCount;
	private String fromConsole;
	private boolean consoleBlock;
	private java.util.List initList;
	
	private void doFrame()
		{
		initList = new ArrayList();
		getKeywordsAndOperators();
		fileChooser = new JFileChooser();
    	filter = new ExampleFileFilter();
    	filter.addExtension("txt");
    	filter.addExtension("c");
    	filter.addExtension("cpp");
    	filter.addExtension("jcpp");
    	//filter.addExtension("gif");
    	filter.setDescription("c, cpp, jcpp, txt files");
	    fileChooser.setFileFilter(filter);
		
		fileChooser.setCurrentDirectory(new File("."));
		try
			{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		catch (Exception e){System.out.println(e);}
		frame = new JFrame("C++ Compiler");
		codeArea = new JTextArea();
		codeArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		codeArea.addCaretListener(new CaretListener()
			{
			public void	caretUpdate(CaretEvent e)
				{
				consoleButtonStart.setEnabled(false);
				}
			});
		toolkit = Toolkit.getDefaultToolkit();
		frame.setSize(toolkit.getScreenSize());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setLocation(0, 0);
		panelUp = new JPanel();
		panelUp.setBackground(Color.green);
		panelUp.setLayout(new GridLayout(10, 1));
		buttonUpCompile = new JButton("Compile");
		buttonUpCompile.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				compile();
				}
			});
		buttonShowConsole = new JButton("Show Console");
		buttonShowConsole.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				frameConsole.setVisible(true);
				}
			});
	//	buttonUpCompile.setLocation(20, 20);
	//	buttonUpCompile.setSize(100, 30);
		panelUp.add(buttonUpCompile);
		panelUp.add(buttonShowConsole);
		/*debugTextArea = new JTextArea();
		debugTextArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		debugTextArea.setBackground(Color.red.darker());
		debugTextArea.setForeground(Color.white);*/
		debugPanelInside = new JPanel();
		debugPanelInside.setLayout(null);
		panelCenter = new JPanel();
		panelCenter.setLayout(new GridLayout(1, 1));
		panelDown = new JPanel();
		panelDown.setLayout(new GridLayout(1, 1));
		panelLeft = new JPanel();
		//codeArea = new JTextArea();
		//codeArea.setSelectionColor(Color.orange);
		messageArea = new JTextArea();
		//panelDown.add(messageArea);
		centerPane = new JScrollPane(codeArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		debugPane = new JScrollPane(debugPanelInside, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPane, messageArea);
		splitPane.setResizeWeight(0.9);
		//splitPane.setdi
		panelCenter.add(splitPane);
		panelTree = new JPanel();
		jTree = createTree();
		panelTree.setLayout(new GridLayout(1, 1));
		panelTree.add(jTree);
		panelDebug = new JPanel();
		panelDebug.setLayout(new BorderLayout());
		panelDebug.add(debugPane, BorderLayout.CENTER);
		jT = new JTabbedPane(3);
		jT.setTabPlacement(JTabbedPane.TOP);
		jT.addTab("Code", panelCenter);
		jT.addTab("Tree", panelTree);
		jT.addTab("Debug", panelDebug);
		//messageArea.setPreferredSize(new Dimension(100, 100));
		//frame.getContentPane().add(panelCenter, BorderLayout.CENTER);
		frame.getContentPane().add(panelUp, BorderLayout.WEST);
		frame.getContentPane().add(jT, BorderLayout.CENTER);
		frame.getContentPane().add(panelDown, BorderLayout.SOUTH);
		frame.addWindowListener(new WindowAdapter()
			{
			public void windowClosing(WindowEvent e)
				{
				System.exit(0);
				}
			});
		openFileMenu = new JMenuItem("Open");
		openFileMenu.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				//fileChooser
				int returnVal = fileChooser.showOpenDialog(frame);
  				if(returnVal == JFileChooser.APPROVE_OPTION) openFile(fileChooser.getSelectedFile().toString());
  				//codeArea.setSelectedTextColor(Color.red);
  				//codePane.setCaretPosition(0);
				}
			});
		saveFileMenu = new JMenuItem("Save");
		saveFileMenu.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				int returnVal = fileChooser.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) saveFile(fileChooser.getSelectedFile().toString());
				}
			});
		fileMenu = new JMenu("File");
		fileMenu.add(openFileMenu);
		fileMenu.add(saveFileMenu);
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		//openFile(".\\Automat.cpp");
		//codeArea.setText("while(1) while(3)k;while(2) {d;k;}");
		doConsoleFrame();
		
		this.openFile("a.txt");
		
		frame.setVisible(true);
		}
	public void setInitList(java.util.List newInitList)
		{
		//System.out.println("SET");
		int different = 0;
		different = newInitList.size() - initList.size();
		//System.out.println("different " + different + " " + newInitList.size() + " " + initList.size());
		JLabel label1, label2;
		if (different > 0) debugPanelInside.setLayout(new GridLayout(newInitList.size(), 1));
		for (int i=0;i<different;i++)
			{
			label1 = new JLabel("");
			debugPanelInside.add(label1);
			debugPanelInside.validate();			
			}
		initList.clear();
		initList.addAll(newInitList);
		Init initTemp;
		for (int i=0;i<initList.size();i++)
			{
			initTemp = (Init)initList.get(i);
			//System.out.println(debugPanelInside.getComponentCount() + " @@@@@@@@@@@ " + i + " initNumber = " + initList.size());
			((JLabel)(debugPanelInside.getComponent(i))).setText("   (" + initTemp.getStringType() + ") " + initTemp.getName() + " = " + initTemp.getValueString());
			//((JLabel)(debugPanelInside.getComponent((int)(i * 2) + 1))).setText(initTemp.getValueString());
			debugPanelInside.validate();
			}
		}
	private void doConsoleFrame()
		{
		frameConsole = new JFrame("Console");
		frameConsole.setSize(500, 400);
		frameConsole.setLocation(50, 50);
		frameConsole.getContentPane().setLayout(new BorderLayout());
		consoleArea = new JTextArea();
		consoleArea.setForeground(Color.white);
		consoleArea.setBackground(Color.black);
		consoleArea.setCaretColor(Color.white);
		consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		consoleArea.setEditable(false);
		consoleField = new JTextField();
		consoleField.setForeground(Color.white);
		consoleField.setBackground(Color.black);
		consoleField.setCaretColor(Color.white);		
		consoleField.setFont(new Font("Monospaced", Font.PLAIN, 15));
		consoleField.addKeyListener(new KeyAdapter()
			{
			public void keyTyped(KeyEvent e)
				{
				if (String.valueOf(e.getKeyChar()).equals("\n"))
					{
					consoleArea.append(consoleField.getText() + "\n");
					fromConsole = consoleField.getText();
					consoleField.setText("");
					consoleBlock = false;
					}
				}
			});
		consoleScroll = new JScrollPane(consoleArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);;
		consolePanelUp = new JPanel();
		consoleButtonStart = new JButton("START");
		consoleButtonStart.addActionListener(new ActionListener()
			{
			public void actionPerformed(ActionEvent e)
				{
				code.resetInitList();
				consoleArea.setText("");
				make();
				}
			});
		consoleButtonPause = new JButton("PAUSE");
		consoleScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 100, 100, 0, 1100);
		consoleLabel = new JLabel("sleep: ");
		consoleLabel.setHorizontalAlignment(JLabel.RIGHT);
		consoleCheck = new JCheckBox("debug", true);
		consolePanelUp.setLayout(new GridLayout(1, 5));
		consolePanelUp.add(consoleButtonStart);
		consolePanelUp.add(consoleButtonPause);
		consolePanelUp.add(consoleLabel);
		consolePanelUp.add(consoleScrollBar);
		consolePanelUp.add(consoleCheck);
		frameConsole.getContentPane().add(consoleScroll, BorderLayout.CENTER);
		frameConsole.getContentPane().add(consolePanelUp, BorderLayout.NORTH);
		frameConsole.getContentPane().add(consoleField, BorderLayout.SOUTH);
		frameConsole.addWindowListener(new WindowAdapter()
			{
			public void windowClosing(WindowEvent e)
				{
				frameConsole.dispose();
				}
			});
		}
	private int showErrorPlace()
		{
		int endLine, startStop;
		int endPlace = -1, startPlace = -1;
		int endLineCount = 0, startStopCount = 0;
		int nextLine = 0, nextLineCount = 0;
		if (code.getLastLine() != null)
			{
			System.out.println("+++++++++++++++++++++++++++++++++++++++++");
			endLine = code.getLastLine().getHowManyEnds();
			startStop = code.getLastLine().getHowManyStartStop();
			for (int i=0;i<codeArea.getText().length();i++)
				{
				if (codeArea.getText().indexOf(Operator.END_LINE, i) == i && Line.isOutSide(codeArea.getText(), Operator.END_LINE))
					{
					endLineCount++;
					}
				if (codeArea.getText().indexOf("\n", i) == i && Line.isOutSide(codeArea.getText(), Operator.END_LINE)) nextLineCount++;
				if ((codeArea.getText().indexOf(Operator.KLAMRA_LEFT, i) == i && Line.isOutSide(codeArea.getText(), Operator.KLAMRA_LEFT)) ||
					(codeArea.getText().indexOf(Operator.KLAMRA_RIGHT, i) == i && Line.isOutSide(codeArea.getText(), Operator.KLAMRA_RIGHT)))
					{
					startStopCount++;
					}				
				if (endLineCount == endLine && endPlace == -1)
					{
					endPlace = i;
					nextLine = nextLineCount;
					}
				if (startStopCount == startStop && startPlace == -1)
					{
					startPlace = i;
					nextLine = nextLineCount;
					}											
				}
			if (Math.max(endPlace, startPlace) > -1)
				{
				codeArea.select(Math.max(endPlace, startPlace) - 5, codeArea.getText().indexOf("\n", Math.max(endPlace, startPlace)) + 2); 
				}
			System.out.println("START = " + Math.max(endPlace, startPlace));
			System.out.println("STOP = " + codeArea.getText().indexOf("\n", Math.max(endPlace, startPlace)));
			//codeArea.selectAll();
			}
		return nextLine + 1;			
		}
	public int getSleep()
		{
		return consoleScrollBar.getValue();
		}
	public boolean isDebug()
		{
		return consoleCheck.isSelected();
		}
	public void make()
		{
		messageArea.setText("");
		if (code != null)
			{
			try
				{
				code.make(code.getLineList());
				}
			catch (JCppException e)
				{
				messageArea.append(e.toString() + "\n");
				if (code.getLastLine() != null) messageArea.append("Linia " + String.valueOf(this.showErrorPlace()) + "\n");
				messageArea.append("Koniec\n");
				}
			}
		}
	public void consolePrint(String text)
		{
		consoleArea.append(text);
		}
	public String consoleIn()
		{
		//frameConsole.setf
		//consoleBlock = true;
		
		return fromConsole;
		}
	public boolean getConsoleBlock()
		{
		return consoleBlock;
		}		
	public void setConsoleBlock(boolean newConsoleBlock)
		{
		consoleBlock = newConsoleBlock;
		}
	public void compile()
		{
		debugPanelInside.removeAll();
		code = new Code(codeArea.getText());
		code.setCCompiler(this);
		messageArea.setText("");
		errorCount = 0;
		consoleButtonStart.setEnabled(true);
		try
			{
			code.setLineList(code.doFineLines());
			}
		catch (JCppException e)
			{
			messageArea.append(e.toString() + "\n");
			consoleButtonStart.setEnabled(false);
			this.showErrorPlace();
			errorCount++;
			}
		messageArea.append("Koniec\n");
		messageArea.append("B��d�w " + String.valueOf(errorCount) + "\n");
		jTree = createTree();
		panelTree.removeAll();
		panelTree.add(jTree);
		}
	private DefaultMutableTreeNode appendNode(Line line, DefaultMutableTreeNode node)
		{
		if (line.getSmallLineList() != null)
			{
			Line nextLine;
			for (int i=0;i<line.getSmallLineList().size();i++)
				{
				nextLine = (Line)line.getSmallLineList().get(i);
				node.add(new DefaultMutableTreeNode(nextLine.getLine()));
				if (nextLine.getSmallLineList() != null) this.appendNode(nextLine, (DefaultMutableTreeNode)node.getChildAt(node.getChildCount() - 1));
				}
			}
		return node;
		}
	private JTree createTree()
		{
		JTree out = null;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Sentence");
		DefaultMutableTreeNode node, nodeBigger;
		Line lineTemp;
		if (code != null && code.getLineList() != null)
			{
			for (int i=0;i<code.getLineList().size();i++)
				{
				lineTemp = (Line)code.getLineList().get(i);
				node = new DefaultMutableTreeNode(lineTemp.getLine());				
				node = this.appendNode(lineTemp, node);
				root.add(node);
				}
			}
		out = new JTree(new DefaultTreeModel(root));
		return out;
		}
	private void getKeywordsAndOperators()
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
			c = Class.forName("Operator");
			fields = c.getFields();
			Operator operator = new Operator();
			operators = new String[fields.length];
			for (int i=0;i<fields.length;i++)
				{
				operators[i] = (String)fields[i].get(operator);
				}
			}
		catch (ClassNotFoundException e){System.out.println(e);}
		catch (IllegalAccessException e){System.out.println(e);}		
		}
	private String getNumber(int number)
		{
		String out = String.valueOf(number);
		if (out.length() == 1) out = "   " + out + " ";
		if (out.length() == 2) out = "  " + out + " ";
		if (out.length() == 3) out = " " + out + " ";		
		return out;
		}
	private boolean saveFile(String fileName)
		{
		boolean out = true;
		try
			{
			PrintWriter pW = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			pW.print(codeArea.getText());
			pW.close();
			}
		catch (IOException e){out = false;}
		return out;
		}
	private boolean openFile(String fileName)
		{
		boolean out = true;
		consoleButtonStart.setEnabled(false);
		try
			{
			BufferedReader bR = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			codeArea.setText("");
			while ((line = bR.readLine()) != null)
				{
				codeArea.append(line + "\n");
				}
			bR.close();
			}
		catch (IOException e){out = false;}
		return out;
		}
	public static void main(String[] argv)
		{
		CCompiler cCompiler = new CCompiler();
		cCompiler.doFrame();
		}
	}