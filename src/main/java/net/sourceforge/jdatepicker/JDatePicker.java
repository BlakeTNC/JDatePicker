package net.sourceforge.jdatepicker;

import java.awt.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Timer;
import java.util.*;
import java.lang.String;
import javax.sound.midi.*;
import java.io.*;


/**
 * @author Juan Heyns
 *
 * Created on 25-Mar-2004
 * Refactored 21-Jun-2004
 * 
 */
public class JDatePicker extends JPanel {
	
	/**
	 * Tests the JDatePicker 
	 * @param args
	 */
	public static void main (String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) { }
		JFrame testFrame = new JFrame();
		testFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		testFrame.setSize(250,350);
		JPanel jPanel = new JPanel();
    JEventNotification();
		jPanel.add(new JDatePicker());
    JPanel DatePanel = new JPanel();
    DatePanel.setLayout(new BorderLayout());
    DatePanel.add(jPanel, BorderLayout.WEST);
    DatePanel.add(eventNotification, BorderLayout.SOUTH);
    BorderLayout fb = new BorderLayout();
    testFrame.setLayout(fb);
    testFrame.getContentPane().add(jmb, fb.NORTH);
		testFrame.getContentPane().add(DatePanel, fb.WEST);
		testFrame.setVisible(true);
	}

	private EventHandler eventHandler;
	private Component ownerComponent;
	private Popup popup;
	private JTextField dateTextField;
	private JButton editButton;
	private static JDatePanel datePanel;

	/**
	 * Constructs a JDatePicker
	 *
	 */
	public JDatePicker(){
		//Call Super Constructor
		super();
		
		//Initialise Variables
		popup = null;
		datePanel = new JDatePanel();
		datePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		eventHandler = new EventHandler();

		//Create Layout
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		//Create and Add Components
		//Add and Configure TextField
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		dateTextField = new JTextField(datePanel.toString());
		gridbag.setConstraints(dateTextField, c);
		dateTextField.setEditable(false);
		dateTextField.setHorizontalAlignment(JTextField.CENTER);
		add(dateTextField);

		//Add and Configure Button
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		editButton = new JButton("...");
		gridbag.setConstraints(dateTextField, c);
		add(editButton);
		
		//Do layout formatting
		setLayout(gridbag);
		int h = (int)editButton.getPreferredSize().getHeight();
		int w = (int)datePanel.getPreferredSize().getWidth();
		editButton.setPreferredSize(new Dimension(h,h));
		dateTextField.setPreferredSize(new Dimension(w-h+1,h));
    eventNotification.setNotificationDate(datePanel.toString());

		//Add event listeners
		editButton.addActionListener(eventHandler);
		addHierarchyBoundsListener(eventHandler);
		datePanel.addChangeListener(eventHandler);
		datePanel.addActionListener(eventHandler);
	}
	
	/**
	 * Prints out the date in the format of the datePanel.toString() 
	 */
	public String toString(){
		return datePanel.toString();
	}
	
	/**
	 * Gets the clone Calendar object with the selected date   
	 * @return Calendar
	 */
	public Calendar getCalendarClone(){
		return datePanel.getCalendarClone();
	}
	
	/**
	 * Called internally to popup the dates.
	 *
	 */
	private void showPopup() {
		if (popup == null){
			PopupFactory fac = new PopupFactory();
			Point xy = getLocationOnScreen();
			datePanel.setVisible(true); 
			popup = fac.getPopup(this, datePanel, (int) xy.getX(), (int) (xy.getY()+this.getHeight()-1));
			popup.show();
		}
	}
	
	/**
	 * Called internally to hide the popup dates. 
	 *
	 */
	private void hidePopup() {
		if (popup != null) {
			dateTextField.setText(datePanel.toString());
			popup.hide();
			popup = null;		
		}
	}

	/**
	 * This internal class hides the public event methods from the outside 
	 */
	private class EventHandler implements ActionListener, HierarchyBoundsListener, ChangeListener {

		public void ancestorMoved(HierarchyEvent arg0) {
			hidePopup();
		}

		public void ancestorResized(HierarchyEvent arg0) {
			hidePopup();
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource()==editButton){
				if (popup == null) {
					showPopup();
				}
				else {
					hidePopup();
				}
			} 
			else if (arg0.getSource()==datePanel){
				hidePopup();
			}
		}

		public void stateChanged(ChangeEvent arg0) {
			if (arg0.getSource()==datePanel)
				dateTextField.setText(datePanel.toString());
        eventNotification.setNotificationDate(datePanel.toString());
		}
	}

  private static EventNotification eventNotification;
  private static JPanel menuPanel= new JPanel();
  private static JMenuBar jmb = new JMenuBar();
  private static JMenu menuFile = new JMenu("File");
  private static JMenuItem mSetEvent = new JMenuItem("Set Event");
  private static JMenuItem mViewEvent = new JMenuItem("View Event");
  private static JMenuItem mQuit = new JMenuItem ("Quit");
  public static void JEventNotification(){
    Timer t=new Timer();
    TimerTask tt = new playTimer();
    t.schedule(tt,5000,30000);
    menuFile.add(mSetEvent);
    menuFile.add(mViewEvent);
    menuFile.add(mQuit);
    jmb.add(menuFile);
    eventNotification = new EventNotification();
    MsetEventListener sehM = new MsetEventListener();
    mSetEvent.addActionListener(sehM);
    MviewEventListener vehM = new MviewEventListener();
    mViewEvent.addActionListener(vehM);
    MquitListener qhM = new MquitListener();
    mQuit.addActionListener(qhM);
  }

  /**listener for mSetEvent **/
  private static class MsetEventListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      eventNotification.setEventDialog();
    }
  }

  /**listener for mViewEvent **/
  private static class MviewEventListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      System.out.println("View");
    }
  }

  /**listener for mQuit **/
  private static class MquitListener implements ActionListener {
    public void actionPerformed(ActionEvent event){
      System.exit(1);
    }
  }

  private static JLabel checkDate = new JLabel();
  private static Calendar ct = new GregorianCalendar();
  private static DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
  /** class for timer to notify the event **/
  private static class playTimer extends TimerTask{
    @Override
    public void run(){
      checkDate.setText(ct.get(Calendar.DATE)+" "+converMonth(ct.get(Calendar.MONTH)+1)+" "+ct.get(Calendar.YEAR));
      System.out.println(datePanel.toString());
      Vector<Integer> v_idx = new Vector<Integer>();
      Vector<String> v_description = new Vector<String>();
      for(int i=0;i<eventNotification.events.size();i++){
        Vector<String> v_info = new Vector<String>();
        Vector<String> v_date = new Vector<String>();
        stringToken(v_info, eventNotification.events.get(i));
        stringToken(v_date, checkDate.getText());
        if(v_info.get(1).compareTo(v_date.get(0))==0 && v_info.get(2).compareTo(v_date.get(1))==0 && v_info.get(3).compareTo(v_date.get(2))==0){
          v_idx.add(i);
        }
      }
      if(v_idx.isEmpty()==false){
        String str = "";
        for(int i=0; i<v_idx.size();i++){
          stringToken(v_description, eventNotification.events.get(v_idx.get(i)));
          for(int j=5;j<v_description.size();j++){
            str+=v_description.get(j)+" ";
          }
          str += "\n\n";
        }
        for(int i=v_idx.size()-1; i>=0; i--){
          eventNotification.events.remove((int)v_idx.get(i));
        }
        try{
          eventNotification.outjdp();
        }catch(IOException e) {
          e.printStackTrace();
        }
        if(v_description.get(4).compareTo("1")==0){
          openAlarm();
        }
        JOptionPane.showMessageDialog(null,str,"Event",JOptionPane.INFORMATION_MESSAGE);
      }
    }
    public String converMonth(int m){
      String str_m="";
      if(m==1){
        str_m = "January";
      }else if(m==2){
        str_m = "February";
      }else if(m==3){
        str_m = "March";
      }else if(m==4){
        str_m = "April";
      }else if(m==5){
        str_m = "May";
      }else if(m==6){
        str_m = "June";
      }else if(m==7){
        str_m = "July";
      }else if(m==8){
        str_m = "August";
      }else if(m==9){
        str_m = "September";
      }else if(m==10){
        str_m = "October";
      }else if(m==11){
        str_m = "November";
      }else if(m==12){
        str_m = "December";
      }
      return str_m;
    }
    /* method for get infomation of one line*/
    public void stringToken(Vector<String> v, String t){
      StringTokenizer  st = new StringTokenizer(t);
      while(st.hasMoreTokens()){
        v.add(st.nextToken());
      }
    }
    public void openAlarm() {
      Sequencer sequencer = null;
      Sequence sequence = null;
      Synthesizer synthesizer = null;
      Instrument instruments[];
      MidiChannel channels[];
      MidiChannel cc = null;
      try {
        if (synthesizer == null) {
          if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
            System.out.println("getSynthesizer() failed!");
            return;
          }
        } 
        synthesizer.open();
        sequencer = MidiSystem.getSequencer();
        sequence = new Sequence(Sequence.PPQ, 10);
      } catch (Exception ex) { ex.printStackTrace(); return; }
      Soundbank sb = synthesizer.getDefaultSoundbank();
    
      if (sb != null) {
        instruments = synthesizer.getDefaultSoundbank().getInstruments();
        synthesizer.loadInstrument(instruments[0]);
      }
      MidiChannel midiChannels[] = synthesizer.getChannels();
      cc = midiChannels[13]; 
      cc.noteOn(86, 80);
    }
  }
}
