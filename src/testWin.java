import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

public class testWin extends javax.swing.JFrame{

	static ServerSocket ss;
	static Socket s;
	static InputStreamReader isr;
	static BufferedReader br;
	static String message;
	private JFrame frame;

	private static JTextArea textArea;
	private JTextField txtSendback;
	private static JTextArea connectStatus;
	/**
	 * Launch the application.
	 */
//	Android message
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testWin window = new testWin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			ss = new ServerSocket(7800);
//			s = new Socket("127.0.0.1", 7800);
			s = ss.accept();
			System.out.println(s);
			while (true) {
				isr = new InputStreamReader(s.getInputStream());
				br = new BufferedReader(isr);
				message = br.readLine();
				System.out.println("Android : "+message);
				
				if( textArea.getText().toString().equals("")){
					textArea.setText("Android : "+message);
				}
				else{
					textArea.setText(textArea.getText()+"\nAndroid : "+message);
				}
				s.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Create the application.
	 */
	public testWin() {
		initialize();
	}
	public void Disconnect() {
		connectStatus.setFont(new Font("Monospaced", Font.PLAIN, 20));
		connectStatus.setText("Disconnected!!");
		connectStatus.setBackground(new Color(255, 153, 153));
		state = 0;
	}
	public void Connect() {
		connectStatus.setFont(new Font("Monospaced", Font.PLAIN, 20));
		connectStatus.setText("Connected!!");
		connectStatus.setBackground(new Color(51, 255, 153));
		state = 1;
	}
	public void DeviceNotFound() {
		connectStatus.setFont(new Font("Monospaced", Font.PLAIN, 13));
		connectStatus.setBackground(new Color(255, 153, 153));
		connectStatus.setText("Connect to Device First --->");
		state = 0;
	}
	private static boolean TCPconnection(){
		try {
			Process p = Runtime.getRuntime().exec("C:\\Users\\BenzRST\\Desktop\\adb.exe forward tcp:7801 tcp:8000");
//			Process p = Runtime.getRuntime().exec("..\\adb.exe forward tcp:7801 tcp:8000");
			Scanner sc = new Scanner(p.getErrorStream());
			if (sc.hasNext()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	static int state = 0;
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 713, 368);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textArea = new JTextArea();
		connectStatus = new JTextArea();
		connectStatus.setFont(new Font("Monospaced", Font.PLAIN, 20));
		connectStatus.setBackground(new Color(255, 153, 153));
		JScrollPane scrollPane = new JScrollPane();
		JButton btnSendToAndroid = new JButton("Send to Android");
		
		btnSendToAndroid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (state == 1&&TCPconnection()) {
					try {
//						source not found
						Socket s = new Socket("127.0.0.1",7801);
						PrintWriter pw = new PrintWriter(s.getOutputStream());
						String PCmessage = txtSendback.getText().trim();
						if (PCmessage.compareToIgnoreCase("")==0) {
							PCmessage = " ";
						}
						System.out.println("Message : "+PCmessage);
						if( textArea.getText().toString().equals("")){
							textArea.setText("PC : "+PCmessage);
						}
						else{
							textArea.setText(textArea.getText()+"\nPC : "+PCmessage);
						}
						pw.write(PCmessage);
						pw.flush();
						pw.close();
						s.close();
						txtSendback.setText(" ");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					DeviceNotFound();
				}
			}
		});
		
		txtSendback = new JTextField();
		txtSendback.setColumns(10);
		
		JButton btnConnectToDevice = new JButton("Connect to Device");
		btnConnectToDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(TCPconnection()){
					Connect();
				}
				else{
					Disconnect();
				}
			}
		});
		
		JLabel lblMessage = new JLabel("Message :");
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(connectStatus, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnConnectToDevice, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
							.addGap(124)
							.addComponent(btnSendToAndroid, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
							.addGap(22))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblMessage, GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtSendback, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)))
							.addGap(9))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtSendback, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
						.addComponent(lblMessage, GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(connectStatus, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnSendToAndroid, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
							.addComponent(btnConnectToDevice, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)))
					.addGap(26))
		);
		
//		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		frame.getContentPane().setLayout(groupLayout);
	}
}
