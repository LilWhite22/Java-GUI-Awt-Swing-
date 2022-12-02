package com.tt.Textbook;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class textbook
{
	public static void main(String[] args) 
	{
		FirstFrame firstFrame=new FirstFrame();		
	}
}

//初始页面
class FirstFrame extends JFrame
{
	public FirstFrame(){
		//container分为上下表格布局
		Container FirstContainer = this.getContentPane();
		FirstContainer.setLayout( new GridLayout(2,1) );
		
		//需要两个面板，分别是标题和退出框以及进入按钮
		Panel panel1 = new Panel( new BorderLayout() );
		Panel panel2 = new Panel( new GridLayout(1,3) );//下部分的1行3列表格布局
		Panel panel3 = new Panel( new GridLayout(2,1) );//下面中间的二层布局
		Panel panel4 = new Panel( new FlowLayout() );//下中之一
		Panel panel5 = new Panel( new FlowLayout() );//下中之二
		//导入标题图片
		ImageIcon Titleimg = new ImageIcon( "photo/title.png" );
		JLabel l1 = new JLabel();
		l1.setIcon( Titleimg );
		JLabel l2 = new JLabel();
		l2.setIcon( new ImageIcon("............") );
		JLabel l3 = new JLabel();
		l3.setIcon( new ImageIcon("..............") );
		panel1.add( l1, BorderLayout.CENTER );
		//进入主页面按钮
		JButton b1 = new JButton("退出程序");
		JButton b2 = new JButton("进入主页面 ");
		panel2.add( l2 );

		panel4.add( b2 );
		panel5.add( b1 );
		
		panel3.add( panel4 );
		panel3.add( panel5 );
		
		panel2.add( panel3 );

		panel2.add( l3 );
		//container添加两个面板
		FirstContainer.add( panel1 );
		FirstContainer.add( panel2 );
		
		b1.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.exit(0);
			}
		});
		
		b2.addActionListener(new ActionListener() //添加监听事件，点击后可以打开主Frame
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				new MainFrame();
				dispose();
			}
		});
		
		
		//一些初始化操作
		this.setVisible(true);
		this.setBounds(500,300,700,500);
		this.setTitle( "桌面记事本" );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		this.setResizable( false );
	}
}

//主要界面
class MainFrame extends JFrame 
{
	private String content;
	private JComboBox YearBox=new JComboBox();
	private JComboBox MonthBox=new JComboBox();
	//两个标签，在init中设置字体样式
	private  JLabel YearLabel=new JLabel("年份：");
	private  JLabel MonthLabel=new JLabel("月份：");
	//两个按钮，需要满足监听功能
	private	JButton b_ok=new JButton("确定");
	private	JButton b_today=new JButton("今天");
	

	//前后两个按钮
//	private JButton upJButton=new JButton("Last");
//	private JButton downJButton=new JButton("Next");
	private JButton upJButton=new JButton( new ImageIcon("photo/line_left.png"));
	private JButton downJButton=new JButton(new ImageIcon("photo/line_right.png"));
	
	//使用日期类，获取当前年份，月份，日期
	private Date date = new Date();//属于一个工具对象，对日期的操作。
	//日期类的get方法获取当前时间
	private int NowYear = date.getYear() + 1900;
	private int NowMonth = date.getMonth();
	private boolean todayFlag = false;//标志开始设置成false是因为有一个初始化
	
	private JButton[] b_week=new JButton[7];
	private	ItemButton[] b_day=new ItemButton[42];
	private String[] week= {"MON","TUE","WED","THU","FRI","SAT","SUN"};
	
	private int Month=0;
	private String Year=null;
	
	private static JLabel BeijingTime;
	private Calendar nowTime=Calendar.getInstance();
	
	private static String sday;
	public void init()							//初始化
	{
		Container containerMain=this.getContentPane();
		containerMain.setLayout(new BorderLayout());
		//首先进行页面布局，分为两个panel，操作panel和主日历panel
		JPanel PanelOpreater = new JPanel(new FlowLayout());
		JPanel PanelMain = new JPanel(new GridLayout(7,7,4,4));
		JPanel PanelNow = new JPanel(new FlowLayout());
		
		//可写可不写
		//container1.setLayout(new BorderLayout());
		
		//设置label字体样式
		YearLabel.setFont(new Font("Dialog",Font.BOLD,16));
		MonthLabel.setFont(new Font("Dialog",Font.BOLD,16));
		
		for(int i = NowYear - 20;i <= NowYear + 100;i++)//年份范围为前后二十年
		{
			YearBox.addItem(i+"");
		}
		YearBox.setSelectedIndex(20);//当前年份
		
		for(int i = 1; i <= 12; i++)//仅有12个月
		{
			MonthBox.addItem(i+"");
		}
		MonthBox.setSelectedIndex(NowMonth);
		
		//先把操作面板添加好按钮和标签
		PanelOpreater.add(YearLabel);
		PanelOpreater.add(YearBox);
		PanelOpreater.add(MonthLabel);
		PanelOpreater.add(MonthBox);
		PanelOpreater.add(b_ok);
		PanelOpreater.add(b_today);
		//获取当前类的监听事件，ok和today可以共用一个事件
		b_ok.addActionListener(new FindActionListener());
		b_today.addActionListener(new FindActionListener());
		
		//up和down按钮
		upJButton.addActionListener(new BesideActionListener());
		downJButton.addActionListener(new BesideActionListener());
		
		
		//对星期进行设置
		for( int i=0; i<7; i++ )
		{
			b_week[i] = new JButton( week[i] );
			if( i == 5 || i == 6 )
				b_week[i].setForeground( Color.red );
			PanelMain.add( b_week[i] );
		}
		
		//把day按钮加进去
		for(int i=0; i<42; i++)
		{
			b_day[i]=new ItemButton("");
			PanelMain.add(b_day[i]);
		}
		
		this.SetCalendar();
		
		
		JLabel time = new JLabel();
		time.setForeground(Color.black);
		time.setBounds(30, 0, 500, 130);
		time.setFont(new Font("微软雅黑", Font.BOLD, 30));

		final JLabel varTime = time;
		Timer timeAction = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long timemillis = System.currentTimeMillis();
				// 转换日期显示格式
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				varTime.setText(df.format(new Date(timemillis)));
			}
		});
		timeAction.start();
		PanelNow.add(varTime);

        containerMain.add(PanelOpreater,BorderLayout.NORTH);
		containerMain.add(upJButton,BorderLayout.WEST);
		containerMain.add(PanelMain,BorderLayout.CENTER);
		containerMain.add(downJButton,BorderLayout.EAST);
		containerMain.add(PanelNow,BorderLayout.SOUTH);
//		pack();
	}

	class FindActionListener implements ActionListener
	{

	
			//监听ok和today的事件
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
				//点击通过判断todayflag，true就返回当天，列表框中的内容变化就变成false然后跳转，用到Date类中的函数
				//跳转时，要调用一个SetCalendar函数
				if(e.getSource()==b_ok)
				{
					todayFlag=false;
					SetCalendar();//跳转操作
				}
				if(e.getSource()==b_today)
				{
					todayFlag=true;//先设置成true然后通过SetCalendar方法跳转
					//刷新列表框中的初始值
					YearBox.setSelectedIndex(20);
					MonthBox.setSelectedIndex(NowMonth);
					SetCalendar();
				}
				
			}
		
	}
	


	class BesideActionListener implements ActionListener//侧翼双栏的控制
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			begin:
			if(e.getSource()==upJButton)
			{
				int monthb=MonthBox.getSelectedIndex();
				int year = YearBox.getSelectedIndex();
				if( monthb - 1 != -1) {
					MonthBox.setSelectedIndex(monthb - 1);
					todayFlag = false;
					SetCalendar();
				}
				else {
					MonthBox.setSelectedIndex(11);
					YearBox.setSelectedIndex(year-1);
					todayFlag = false;
					SetCalendar();
				}
			}
			else 
				if(e.getSource()==downJButton)
			{
				int monthb=MonthBox.getSelectedIndex();
				int year = YearBox.getSelectedIndex();
				if( monthb + 1 != 12) {
					MonthBox.setSelectedIndex(monthb + 1);
					todayFlag = false;
					SetCalendar();
				}
				else {
					MonthBox.setSelectedIndex(0);
					YearBox.setSelectedIndex(year+1);
					todayFlag = false;
					SetCalendar();
				}

			}
		}
		
	}
	
	public void SetCalendar()					//需要传入列表框中选中的年份，月份；然后重新设置表格内容
	{
		
		int SumDays=0;
		
		if(todayFlag)//是今天
		{
			Year=NowYear+"";
			Month=NowMonth;
		}
		else//不是今天，根据列表框中的选择
		{
			Year=YearBox.getSelectedItem().toString();//object类中，转成字符串
			Month=MonthBox.getSelectedIndex();//默认设置月份从0开始
		}
		int intYear=Integer.parseInt(Year)-1900;//将字符串转成int型
		Date FirstDay = new Date(intYear, Month, 1);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(FirstDay);
		int counter=1;//计数器，用来设置时间
		if(Month == 0||Month == 2||Month == 4||Month == 6||Month == 7||Month == 9||Month == 11)//
		{
			SumDays = 31;
		} else if(Month == 3||Month == 5||Month == 8||Month == 10) {
			SumDays = 30;
		} else {
			if( cal.isLeapYear(intYear) )    //判断闰年
			{
				SumDays = 29;
			} else {
				SumDays = 28;
			}
		}
		
		int FirstLocation=FirstDay.getDay();
		for(int i=((FirstLocation-1)+7)%7;i<((FirstLocation-1)+7)%7+SumDays;i++)
		{
			if(Month==NowMonth && intYear==NowYear-1900 && i==((FirstLocation-1)+7)%7+date.getDate()-1)
			{
				b_day[i].setForeground(Color.ORANGE);
				b_day[i].setText((counter++)+"");
			}
			else {
				b_day[i].setText((counter++) + "");
				b_day[i].setForeground(Color.black);
			}
			if(i%7==5 || i%7==6)
			{
				if(!(Month==NowMonth && intYear==NowYear-1900 && i==((FirstLocation-1)+7)%7+date.getDate()-1))
					b_day[i].setForeground(Color.red);
			}
		}
		//没有用的地方清空
		for(int i=0;i<((FirstLocation-1)+7)%7;i++)
			b_day[i].setText("");
		for(int i=((FirstLocation-1)+7)%7+SumDays;i<42;i++)
			b_day[i].setText("");
	}

	public MainFrame()
	{
		this.init();

		//此处也可以把初始化封装在一个init方法里
		this.setTitle("日历");
		this.setVisible(true);
		this.setBounds(500, 300, 700 ,500 );
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "确认退出?", "确认", 							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if(result == JOptionPane.OK_OPTION){
					System.exit(0);
				}
			}
		});
	}

	class ItemButton extends JButton			//按钮事件触发
	{
		public ItemButton(String name)
		{
			super();
			this.setText(name);
			this.setVisible(true);
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					sday=e.getActionCommand();
					if( sday!="" )							//防止日期空白按钮被打开写文件
					try {
						new TextJFrame();
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			});
		}
	}

	class TextJFrame extends JFrame				//文本框frame，用来输入待办事项
	{
		public TextJFrame() throws IOException {
			this.setTitle("备忘事项");
			Container TextContainer=this.getContentPane();
			TextContainer.setLayout(new BorderLayout());

//			TextField textTitle=new TextField("请输入待办事件：");
			String syear = (String) YearBox.getSelectedItem();
			String smonth = (String) MonthBox.getSelectedItem();
			String fname = null;											//用于判断的文件名字：是否存在
			if(smonth.length()<2)
			{
				if(sday.length()<2)
					fname = syear+"0"+smonth+"0"+sday;
				else
					fname = syear+"0"+smonth+sday;
			} else {
				if(sday.length()<2)
					fname = syear+smonth+"0"+sday;
				else
					fname = syear+smonth+sday;
			}
			File file = new File("src//"+fname);				//创建文档，可以根据前面传来的 key 修改文件名用于后期的读出

			if (!file.exists()) {										//原文件不存在， 需要新建文件
				file.createNewFile();
				content=null;
				System.out.println("文件已创建");
			} else {													//原文件存在， 可以直接打开 并 续写
				content = TextFile.read("src//"+fname);	    //已经获取文件内容
				System.out.println("文件已存在");
			}

			TextField textMain=new TextField(content);					//文本框所在
			textMain.addActionListener(new TextActionListener() );		//添加监听事件

			//使用一个返回按钮
			JButton ret=new JButton("返回按钮");
			ret.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

//			TextContainer.add(textTitle,BorderLayout.NORTH);
			TextContainer.add(textMain,BorderLayout.CENTER);
			TextContainer.add(ret,BorderLayout.SOUTH);
			//文本窗口的一些初始化工作

			this.setVisible(true);
			this.setBounds(500, 300, 700 ,500 );
			this.setResizable(false);
		}
	}

	class TextActionListener implements ActionListener//内部监听类
	{

		@Override
		public void actionPerformed(ActionEvent e) 						//在这之前先判断同名文件是否存在，如果存在则读出，不存在则新建
		{																//新建文件存储在src文件夹内
			String syear=(String) YearBox.getSelectedItem();
			String smonth=(String) MonthBox.getSelectedItem();
			String fname=null;
			if(smonth.length()<2)
			{
				if(sday.length()<2)
					fname=syear+"0"+smonth+"0"+sday;
				else
					fname=syear+"0"+smonth+sday;
			} else {
				if(sday.length()<2)
					fname=syear+smonth+"0"+sday;
				else
					fname=syear+smonth+sday;
			}
			TextField field=(TextField)e.getSource();						//获得一些资源
			TextFile.write( "src//" + fname, field.getText() );	//将 field 中的内容写入 data.txt
			//				例子测试
			//				public static void main(String[] args) {
			//					String file = read("e:\\data\\data3.txt");			//读出文件
			//					System.out.println(file);							//输出到控制台
			//					write("e:\\data\\data5.txt",file);					//写入到 xx 文件
			//
			//					TextFile text = new TextFile("e:\\data\\data3.txt");		//读出/文件到 TextFile
			//					System.out.println(text);									//输出
			//					text.write("e:\\data\\data6.txt");							//将 TextFile 写入到 xx 路径的文件
			//				}
			//				System.out.println(	field.getText());
			//				field.setText("");
		}
	}
}





