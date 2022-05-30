import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Client extends JFrame {
    private static final String TAG = "Start :";
    // 클라이언트가 입력한 아이디 값을 클라이언트도 알도록 전역 변수로 설정.
    private String IDString;//아이디를 전달해줄 스트링

    private ImageIcon icon_GameStart;
    private ImageIcon icon_BlackPen;
    private ImageIcon icon_RedPen;
    private ImageIcon icon_OrangePen;
    private ImageIcon icon_YellowPen;
    private ImageIcon icon_GreenPen;
    private ImageIcon icon_BluePen;
    private ImageIcon icon_IndigoPen;
    private ImageIcon icon_PurplePen;

    // 통신
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    // 이미지 패널
    private MyPanel p_Main; // 초기 메인 화면 이미지


    // p_Main에 포함됨
    private JButton btn_Start; // 아이디 입력 전 게임시작 버튼

    // btn_Start을 누르면 p_Id이 나타남
    private JPanel p_Id; // 아이디 입력 패널
    private JPanel p_Sub; // 아이디 입력 패널

    // p_Id에 포함됨
    private JLabel la_Id; // '아이디를 입력하세요'라벨
    private TextField tf_IdInput; // 아이디 입력
    private JButton btn_Id; // 아이디 입력 버튼

    // btn_Start을 누르면 p_Draw로 바뀜
    private MyPanel1 p_Draw; // 그리기방 패널(화면 전체)


    //p_MplId
    private JPanel p_TopMpId;

    // 왼쪽을 포함한 중간 그림판
    private JPanel p_MplId; // 그림판



    // 아래쪽 팔레트
    private JPanel p_West; // 팔레트

    private MyButton1 btn_Trash; // 지우개 이미지

    // plWest에 포함됨
    private MyPanel2 p_Palette; // 크레파스 이미지

    private JButton btn_BlackDrawPen;
    private JButton btn_RedDrawPen;
    private JButton btn_OrangeDrawPen;
    private JButton btn_YellowDrawPen;
    private JButton btn_GreenDrawPen;
    private JButton btn_BlueDrawPen;
    private JButton btn_IndigoDrawPen;
    private JButton btn_PurpleDrawPen;

    private MyButton btn_Eraser; // 지우개 이미지

    // 오른쪽 유저목록, 채팅, 준비완료, 나가기 버튼
    private JPanel p_East;
    private JButton btn_Exit; // 나가기 버튼

    // p_East에 포함됨
    private JTextArea ta_UserList; // 유저 목록 라벨

    // p_East에 포함된 채팅 패널
    private JPanel p_Chat; // 채팅창, 채팅 입력란

    // p_Chat에 포함됨
    private TextField tf_Chat; // 채팅 입력
    private JTextArea ta_Chat; // 채팅 로그
    private JScrollPane scr_Chat;

    // 준비완료, 나가기 버튼 패널
    private JPanel btn_Panel; // 채팅창, 채팅 입력란

    // 폰트 크기 설정
    private Font ft_Small; // 16px크기 폰트
    private Font ft_Medium; // 24px크기 폰트
    private Font ft_Large; // 36px크기 폰트

    // Brush 좌표값
    int x, y;
    // Brush 색깔
    // Color color;

    // Draw에 필요한 선언
    private BufferedImage imgBuff;
    private JLabel drawLabel;
    private JPanel drawPanel;
    private Brush brush;
    String sendDraw = null;
    String sendColor = null;
    boolean drawPPAP = true;

    // 이미지 매서드
    private ImageIcon ImageSetSize(ImageIcon icon, int width, int heigth) {
        Image xImage = icon.getImage();
        Image yImage = xImage.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
        ImageIcon xyImage = new ImageIcon(yImage);
        return xyImage;
    }

    // 이미지 삽입용 클래스
    class MyPanel extends JPanel {
        private ImageIcon icon = new ImageIcon("img/main.png");
        private Image imgMain = icon.getImage();

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
        }
    };

    //그림 패널
    class MyPanel1 extends JPanel {
        private ImageIcon icon = new ImageIcon("img/draw.png");
        private Image imgMain = icon.getImage();

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
        }
    };

    class MyPanel2 extends JPanel {
        private ImageIcon icon = new ImageIcon("img/색모음.jpg");
        private Image imgMain = icon.getImage();

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
        }
    };

    class MyPanel3 extends JPanel {

        private ImageIcon icon = new ImageIcon("img/login.png");
        private Image imgMain = icon.getImage();
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
        }
    };

    class MyButton extends JButton {
        private ImageIcon icon = new ImageIcon("img/Eraser.png");
        private Image imgMain = icon.getImage();

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
            setBorderPainted(false); // 버튼 테두리 제거
        }
    };

    class MyButton1 extends JButton {
        private ImageIcon icon = new ImageIcon("img/trash.png");
        private Image imgMain = icon.getImage();

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imgMain, 0, 0, getWidth(), getHeight(), null);
            setBorderPainted(false); // 버튼 테두리 제거
        }
    };

    public Client() {
        init();
        setting();
        batch();
        listener();
        setVisible(true);
    }

    private void init() {
        // 이미지 패널
        p_Main = new MyPanel(); // 초기 메인 화면 이미지
        p_TopMpId = new MyPanel1(); // p_MplId - 그림판 이미지
        p_Palette = new MyPanel2(); // plWest - 크레파스 이미지
        p_Id = new MyPanel3(); //로그인 이미지
        btn_Eraser = new MyButton(); // plWest - 지우개 이미지
        btn_Trash = new MyButton1(); // plWest - 휴지통 이미지

        // 패널
        p_Sub = new JPanel(); // p_Main - 초기 아이디 입력 패널
        p_Draw = new MyPanel1(); // 아이디를 입력하고 버튼을 누르면 나오는 패널, 게임화면 전체

        // plDraw
        p_MplId = new JPanel();
        p_West = new JPanel();
        p_East = new JPanel();
        btn_Panel = new JPanel();

        // p_East
        p_Chat = new JPanel();
        btn_Exit = new JButton(new ImageIcon("img/exit.png"));

        // 이미지
        icon_GameStart = new ImageIcon("img/btnstart.png"); // 게임시작 버튼 이미지
        icon_BlackPen = new ImageIcon("img/black.png");
        icon_RedPen = new ImageIcon("img/red.png");
        icon_OrangePen = new ImageIcon("img/orange.png");
        icon_YellowPen = new ImageIcon("img/yellow.png");
        icon_GreenPen = new ImageIcon("img/green.png");
        icon_BluePen = new ImageIcon("img/blue.png");
        icon_IndigoPen = new ImageIcon("img/indigo.png");
        icon_PurplePen = new ImageIcon("img/purple.png");

        // 버튼
        btn_Start = new JButton(icon_GameStart); // p_Main
        btn_Id = new JButton(icon_GameStart); // p_Main


        btn_BlackDrawPen = new JButton(icon_BlackPen);
        btn_RedDrawPen = new JButton(icon_RedPen);
        btn_OrangeDrawPen = new JButton(icon_OrangePen);
        btn_YellowDrawPen = new JButton(icon_YellowPen);
        btn_GreenDrawPen = new JButton(icon_GreenPen);
        btn_BlueDrawPen = new JButton(icon_BluePen);
        btn_IndigoDrawPen = new JButton(icon_IndigoPen);
        btn_PurpleDrawPen = new JButton(icon_PurplePen);

        // 라벨
        la_Id = new JLabel("아이디"); // p_Main

        // 텍스트 입력란
        tf_IdInput = new TextField(); // p_Main
        tf_Chat = new TextField(); // p_East

        // 텍스트 영역
        ta_Chat = new JTextArea(); // p_East
        ta_UserList = new JTextArea();
        // 폰트
        ft_Small = new Font("맑은고딕", Font.PLAIN, 16);
        ft_Medium = new Font("맑은고딕", Font.PLAIN, 24);
        ft_Large = new Font("맑은고딕", Font.PLAIN, 36);

        // 스크롤 바
        scr_Chat = new JScrollPane(ta_Chat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // 드로우 캔버스
        imgBuff = new BufferedImage(750, 500, BufferedImage.TYPE_INT_ARGB);
        drawLabel = new JLabel(new ImageIcon(imgBuff));
        drawPanel = new JPanel();
        brush = new Brush();
    }

    private void setting() {
        setTitle("내가그린기린그림");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // p_Main
        setContentPane(p_Main);
        p_Main.setLayout(null);
        btn_Start.setBounds(85, 445, 110, 40); // btn_Start 위치, 크기 조정 (x, y, width, height)
        // 버튼 테두리 제거
        btn_Start.setBorderPainted(false);
        btn_Start.setFocusPainted(false);
        btn_Start.setContentAreaFilled(false);

        icon_GameStart = ImageSetSize(icon_GameStart, 110, 40); // 게임 시작 버튼 이미지
        // p_Id
        p_Id.setLayout(null);
        p_Id.setVisible(false); // 비활성화
        p_Id.setBackground(new Color(242, 242, 242));
        p_Id.setBounds(0, 0, 800, 600); // plId 위치, 크기 조정 (x, y, width, height) 좌표는 p_Main 기준

        p_Sub.setLayout(null);
        p_Sub.setVisible(false); // 비활성화
        p_Sub.setBounds(280, 270, 240, 50); // plId 위치, 크기 조정 (x, y, width, height) 좌표는 p_Main 기준

        tf_IdInput.setBounds(0, 0, 240, 50); // tfIdInput 위치, 크기 조정 (x, y, width, height) 좌표는 plId 기준
        tf_IdInput.setBackground(null);
        tf_IdInput.setFont(ft_Medium);

        btn_Id.setBounds(85, 445, 110, 40); // btnId 위치, 크기 조정 (x, y, width, height) 좌표는 plId 기준
        // 버튼 테두리 제거
        btn_Id.setBorderPainted(false);
        btn_Id.setFocusPainted(false);
        btn_Id.setContentAreaFilled(false);

        // p_DrawRoom
        p_Draw.setLayout(null);
        p_Draw.setVisible(false); // 비활성화
        p_Draw.setBounds(0, 0, 1200, 900);// plDrawRoom 위치, 크기 조정 좌표는 p_Main 기준

        // p_DrawRoom - p_TopMpId
        p_TopMpId.setLayout(null);
        p_TopMpId.setBackground(new Color(255, 255, 255, 255));
        p_TopMpId.setBounds(0, 0, 1200, 900);

        // plDrawRoom - p_MplId
        p_MplId.setLayout(null);
        p_MplId.setBackground(new Color(255, 255, 255, 255));
        p_MplId.setBounds(292, 207, 600, 450); // p_MplId 위치, 크기 조정 좌표는 plDrawRoom 기준

        // plDrawRoom - plWest
        p_West.setLayout(null);
        p_West.setBackground(new Color(242, 242, 242, 255));
        p_West.setBounds(45, 198, 200, 580); // plWest 위치, 크기 조정 좌표는 plDrawRoom 기준

        icon_BlackPen = ImageSetSize(icon_BlackPen, 95, 95);
        icon_RedPen = ImageSetSize(icon_RedPen, 95, 95);
        icon_OrangePen = ImageSetSize(icon_OrangePen, 65, 130);
        icon_YellowPen = ImageSetSize(icon_YellowPen, 65, 130);
        icon_GreenPen = ImageSetSize(icon_GreenPen, 65, 130);
        icon_BluePen = ImageSetSize(icon_BluePen, 65, 130);
        icon_IndigoPen = ImageSetSize(icon_IndigoPen, 65, 130);
        icon_PurplePen = ImageSetSize(icon_PurplePen, 65, 130);

        btn_BlackDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_BlackDrawPen.setBounds(0, 0, 95, 95);
        btn_BlackDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_BlackDrawPen.setVisible(true);

        btn_RedDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_RedDrawPen.setBounds(0, 105, 95, 95);
        btn_RedDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_RedDrawPen.setVisible(true);

        btn_OrangeDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_OrangeDrawPen.setBounds(0, 210, 95, 95);
        btn_OrangeDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_OrangeDrawPen.setVisible(true);

        btn_YellowDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_YellowDrawPen.setBounds(0, 315, 95, 95);
        btn_YellowDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_YellowDrawPen.setVisible(true);

        btn_GreenDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_GreenDrawPen.setBounds(105, 0 , 95, 95);
        btn_GreenDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_GreenDrawPen.setVisible(true);

        btn_BlueDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_BlueDrawPen.setBounds(105, 105, 95, 95);
        btn_BlueDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_BlueDrawPen.setVisible(true);

        btn_IndigoDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_IndigoDrawPen.setBounds(105, 210, 95, 95);
        btn_IndigoDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_IndigoDrawPen.setVisible(true);

        btn_PurpleDrawPen.setBackground(new Color(255, 255, 255, 255));
        btn_PurpleDrawPen.setBounds(105, 315, 95, 95);
        btn_PurpleDrawPen.setBorderPainted(false); // 버튼 테두리 제거
        btn_PurpleDrawPen.setVisible(true);

        // p_DrawRoom - p_East
        p_East.setLayout(null);
        p_East.setBounds(926, 130, 255, 600); // p_East 위치, 크기 조정 좌표는 plDrawRoom 기준
        //p_East.setAlwaysOnTop(true);

        // p_DrawRoom - p_Chat
        p_Chat.setLayout(null);

        // p_DrawRoom - btn_Panel
        btn_Panel.setLayout(null);
        btn_Panel.setBackground(new Color(242, 242, 242, 255));
        btn_Panel.setBounds(1075, 540, 405, 130);


        // p_West
        p_Palette.setLayout(null);
        p_Palette.setBackground(new Color(255, 255, 255, 255));
        p_Palette.setBounds(0, 0, 200, 580); // p_Palette 위치, 크기 조정 좌표는 plWest 기준

        btn_Eraser.setBackground(new Color(151, 178, 99, 255));
        btn_Eraser.setBounds(10, 440, 80, 130); // btn_Eraser 위치, 크기 조정 좌표는 plWest 기준


        btn_Trash.setBackground(new Color(151, 178, 99, 255));
        btn_Trash.setBounds(105, 440, 95, 140); // btn_Eraser 위치, 크기 조정 좌표는 plWest 기준

        // p_East
        ta_UserList.setBounds(0, 0, 255, 150); // ta_UserList 위치, 크기 조정 좌표는 p_East 기준
        ta_UserList.setFont(ft_Medium);
        ta_UserList.setBackground(new Color(242, 242, 242, 255));
        ta_UserList.setLineWrap(true);

        p_Chat.setBackground(Color.WHITE);
        p_Chat.setBounds(0, 150, 255, 385); // p_Chat 위치, 크기 조정 좌표는 p_East 기준

        btn_Exit.setBounds(0, 535, 255, 65); // btn_Exit 위치, 크기 조정 좌표는 p_East 기준
        btn_Exit.setFont(ft_Medium);
        btn_Exit.setBackground(new Color(151, 178, 99, 255));
        btn_Exit.setBorderPainted(false);


        // p_East - p_Chat
        tf_Chat.setBackground(Color.WHITE);
        tf_Chat.setBounds(0, 350, 255, 30); // tf_Chat 위치, 크기 조정 좌표는 p_East 기준
        tf_Chat.setFont(ft_Medium);
        tf_Chat.setBackground(new Color(242, 242, 242, 255));
        tf_Chat.setColumns(30);

        scr_Chat.setBounds(0, 0, 255, 350); // ta_Chat 위치, 크기 조정 좌표는 p_East 기준
        scr_Chat.setFocusable(false);

        ta_Chat.setLineWrap(true);
        ta_Chat.setBackground(new Color(242, 242, 242, 255));

        // 드로우 캔버스
        drawLabel.setBounds(0, 0, 750, 500);
        drawLabel.setBackground(new Color(255, 0, 255, 0));
        brush.setBounds(0, 0, 750, 500);

        setSize(800, 640);
    }

    private void batch() {
        p_Main.add(btn_Start);
        p_Main.add(p_Id);
        p_Main.add(p_Draw);
        btn_Start.setIcon(icon_GameStart);

        p_Id.add(p_Sub);
        p_Sub.add(la_Id);
        p_Sub.add(tf_IdInput);
        p_Id.add(btn_Id);
        btn_Id.setIcon(icon_GameStart);

        p_Draw.add(p_TopMpId);

        p_Draw.add(p_MplId);

        p_Draw.add(p_West);
        p_TopMpId.add(p_East);
        p_Draw.add(btn_Panel);


        p_West.add(p_Palette);
        p_West.add(btn_Eraser);
        p_West.add(btn_Trash);

        p_Palette.add(btn_BlackDrawPen);
        p_Palette.add(btn_RedDrawPen);
        p_Palette.add(btn_OrangeDrawPen);
        p_Palette.add(btn_YellowDrawPen);
        p_Palette.add(btn_GreenDrawPen);
        p_Palette.add(btn_BlueDrawPen);
        p_Palette.add(btn_IndigoDrawPen);
        p_Palette.add(btn_PurpleDrawPen);

        p_East.add(p_Chat);
        p_East.add(ta_UserList);
        p_East.add(btn_Exit);

        p_Chat.add(scr_Chat);
        p_Chat.add(tf_Chat);


        // 드로우
        p_MplId.add(drawLabel);
        p_MplId.add(brush);

    }

    private void listener() {
        // Enter 입력시 채팅 메세지가 보내지는 이벤트.
        tf_Chat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendChat();
            }
        });

        // 이 이벤트로 plId이 활성화 되어서 아이디를 입력할 수 있음.
        btn_Start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn_Start = (JButton) e.getSource();
                p_Id.setVisible(true); // plId 활성화
                p_Sub.setVisible(true); // plId 활성화
                btn_Start.setVisible(false); // btn_Start 비활성화
            }
        });

        // 이 이벤트로 plDrawRoom이 활성화 되어서 그리기방에 입장함.
        btn_Id.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // JButton btnId = (JButton)e.getSource();
                connectServer(); // 서버와 연결
                sendInsertId();
            }
        });

        //나가기 버튼
        btn_Exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendExit();
                System.exit(0);
            }
        });

        // 마우스를 눌렀을때 그리는 이벤트

        drawLabel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (drawPPAP == true) {
                    System.out.println("ppap true 실행 됨");
                    sendDraw = "DRAW&" + e.getX() + "," + e.getY();
                    brush.xx = e.getX();
                    brush.yy = e.getY();
                    brush.repaint();
                    brush.printAll(imgBuff.getGraphics());
                    writer.println(sendDraw);
                } else {
                    System.out.println("ppap false 실행 됨");
                }
            }
        });

        // 검은색 펜 이벤트
        btn_BlackDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Black";
                brush.setColor(Color.BLACK);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 빨간색 펜 이벤트
        btn_RedDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Red";
                brush.setColor(Color.RED);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 오렌지색 펜 이벤트
        btn_OrangeDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Orange";
                brush.setColor(Color.ORANGE);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 노란색 펜 이벤트
        btn_YellowDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Yellow";
                brush.setColor(Color.YELLOW);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 초록색 펜 이벤트
        btn_GreenDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Green";
                brush.setColor(Color.GREEN);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 하늘색 펜 이벤트
        btn_BlueDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Blue";
                brush.setColor(Color.CYAN);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 파란색 펜 이벤트
        btn_IndigoDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Indigo";
                brush.setColor(Color.BLUE);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 보라색 펜 이벤트
        btn_PurpleDrawPen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "Purple";
                brush.setColor(new Color(102, 0, 153));
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 지우개(흰색) 이벤트
        btn_Eraser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendColor = "COLOR&" + "White";
                brush.setColor(Color.WHITE);
                writer.println(sendColor);
                System.out.println("색 변경 : " + sendColor);
            }
        });
        // 드로우 캔버스 초기화 이벤트
        btn_Trash.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("delete 버튼 눌러짐");
                sendColor = "COLOR&" + "Delete";
                writer.println(sendColor);
                brush.setClearC(false);
                cleanDraw();
                System.out.println("드로우 캔버스 초기화");
            }
        });
    }

    // 접속 시 서버 연결 메서드.
    private void connectServer() {
        try {
            socket = new Socket("localhost", 13389);
            ReaderThread rt = new ReaderThread();
            rt.start();
        } catch (Exception e) {
            System.out.println(TAG + "서버 연결 실패");
        }
    }

    // CHAT 프로토콜 메서드.
    private void sendChat() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            String chatString = tf_Chat.getText();
            writer.println("CHAT&" + chatString);
            tf_Chat.setText("");
        } catch (Exception e) {
            System.out.println(TAG + "채팅 메세지 요청 실패");
        }
    }

    // ID 프로토콜 메서드
    private void sendInsertId() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            IDString = tf_IdInput.getText();
            if ((IDString.equals(""))) { // NULL값 입력시
                IDString = "emptyID";
                writer.println("ID&" + IDString);
                p_Id.setVisible(false); // plId 비활성화
                p_Sub.setVisible(false); // plId 활성화
                p_Draw.setVisible(true); // plDrawRoom 활성화
                setSize(1200, 900);
            } else { // 아이디 값 입력시.
                writer.println("ID&" + IDString);
                tf_IdInput.setText("");
                p_Id.setVisible(false); // plId 비활성화
                p_Sub.setVisible(false); // plId 활성화
                p_Draw.setVisible(true); // plDrawRoom 활성화
                setSize(1200, 900);
            }

        } catch (IOException e) {
            System.out.println(TAG + "준비 메세지 요청 실패");
        }
    }

    // 드로우 캔버스 초기화 메서드
    private void cleanDraw() {
        brush.setClearC(false);
        brush.repaint();
        brush.printAll(imgBuff.getGraphics());
    }

    // EXIT 프로토콜 메서드.
    private void sendExit() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("EXIT&" + IDString);
        } catch (Exception e) {
            System.out.println(TAG + "Exit Msg writer fail...");
        }
    }

    // 서버로 부터 메세지를 받아 TextArea에 뿌려주는 Thread.
    class ReaderThread extends Thread {
        private BufferedReader reader;

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String readerMsg = null;
                String[] parsReaderMsg;
                while ((readerMsg = reader.readLine()) != null) {
                    parsReaderMsg = readerMsg.split("&");
                    if (parsReaderMsg[0].equals("DRAW")) {
                        String[] drawM = parsReaderMsg[1].split(",");
                        x = Integer.parseInt(drawM[0]);
                        y = Integer.parseInt(drawM[1]);
                        brush.setX(x);
                        brush.setY(y);
                        brush.repaint();
                        brush.printAll(imgBuff.getGraphics());
                        System.out.println("브러쉬 값 : " + drawM);
                        System.out.println("브러쉬 값 : " + x);
                        System.out.println("브러쉬 값 : " + y);
                    } else if (parsReaderMsg[0].equals("COLOR")) {
                        System.out.println("색 변경 요청 들어옴");
                        if (parsReaderMsg[1].equals("Black")) {
                            System.out.println("검은색 요청");
                            brush.setColor(Color.BLACK);
                        } else if (parsReaderMsg[1].equals("Red")) {
                            System.out.println("빨간색 요청");
                            brush.setColor(Color.RED);
                        } else if (parsReaderMsg[1].equals("Orange")) {
                            System.out.println("주황색 요청");
                            brush.setColor(Color.ORANGE);
                        } else if (parsReaderMsg[1].equals("Yellow")) {
                            System.out.println("노랑색 요청");
                            brush.setColor(Color.YELLOW);
                        } else if (parsReaderMsg[1].equals("Green")) {
                            System.out.println("초록색 요청");
                            brush.setColor(Color.GREEN);
                        } else if (parsReaderMsg[1].equals("Blue")) {
                            System.out.println("파랑색 요청");
                            brush.setColor(Color.CYAN);
                        } else if (parsReaderMsg[1].equals("Indigo")) {
                            System.out.println("남색 요청");
                            brush.setColor(Color.BLUE);
                        } else if (parsReaderMsg[1].equals("Purple")) {
                            System.out.println("보라색 요청");
                            brush.setColor(new Color(102, 0, 153));
                        } else if (parsReaderMsg[1].equals("White")) {
                            System.out.println("지우개 요청");
                            brush.setColor(Color.WHITE);
                        } else if (parsReaderMsg[1].equals("Delete")) {
                            System.out.println("화면 리셋 요청");
                            brush.setClearC(false);
                            brush.repaint();
                            brush.printAll(imgBuff.getGraphics());
                        }
                    } else if (parsReaderMsg[0].equals("SERVER")) {
                        ta_Chat.append("[서버]: " + parsReaderMsg[1] + "\n");
                    } else if (parsReaderMsg[0].equals("CHAT") && parsReaderMsg.length > 1) {
                        ta_Chat.append(parsReaderMsg[1] + "\n");
                    } else if (parsReaderMsg[0].equals("ID")) {
                        ta_UserList.setText("");
                    } else if (parsReaderMsg[0].equals("IDLIST")) {
                        ta_UserList.append(parsReaderMsg[1] + "\n");
                    }    else {
                        ta_Chat.append("\n");
                    }
                    // 스크롤을 밑으로 고정.
                    scr_Chat.getVerticalScrollBar().setValue(scr_Chat.getVerticalScrollBar().getMaximum());
                }
            } catch (IOException e) {
                System.out.println(TAG + "통신 실패");
            }
        }
    }

    // 그리기 위한 펜을 만들어 주는 클래스
    class Brush extends JLabel {
        public int xx, yy;
        public Color color = Color.BLACK;
        public boolean drawPen = true;
        public boolean clearC = true;

        @Override
        public void paint(Graphics g) {
            if (drawPen == true) {
                g.setColor(color);
                g.fillOval(xx - 10, yy - 10, 10, 10);
                System.out.println(drawPPAP);
            } else if (drawPen == false) {
                g.setColor(Color.WHITE);
                g.fillOval(0, 0, 0, 0);
                System.out.println(drawPPAP);
                System.out.println("브러쉬 사용 못 하게 막음");
            }
            if (clearC == true) {
                g.setColor(color);
                g.fillOval(xx - 10, yy - 10, 10, 10);
            } else if (clearC == false) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, 1000, 1000);
                clearC = true;
                System.out.println("캔버스 클리어 실행됨");
            }

        }

        public void setX(int x) {
            this.xx = x;
        }

        public void setY(int y) {
            this.yy = y;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void setDrawPen(boolean drawPen) {
            this.drawPen = drawPen;
        }

        public void setClearC(boolean clearC) {
            this.clearC = clearC;
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
