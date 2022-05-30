import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private static final String TAG = "GameServer : "; // 태그 생성.
    private Vector<ClientInfo> vcClient; // 클라이언트의 정보를 담는 벡터.
    private ServerSocket serverSocket; // 서버 소켓.
    private Socket socket; // 클라이언트가 접속하면 새로 만드는 소켓.

    public class protocol {

        String SERVER = "SERVER";
        String CHAT = "CHAT";
        String ID = "ID";
        String IDLIST = "IDLIST";
        String DRAW = "DRAW";
        String COLOR = "COLOR";
    }

    // 생성자에서 클라이언트의 접속을 대기한다 (메인 스레드)
    public Server() {
        try {
            // 서버 소켓의 포트를 '13389'으로 설정.
            serverSocket = new ServerSocket(13389);
            vcClient = new Vector<>();
            while (true) {
                System.out.println("클라이언트 요청 대기중.....");
                socket = serverSocket.accept(); // 서버의 접속을 대기중.
                System.out.println("요청이 성공함");
                // 추가 스레드에 클라이언트 소켓을 타켓으로 설정.
                ClientInfo ci = new ClientInfo(socket);
                ci.start();
                vcClient.add(ci); // 백터에 추가.

            }

        } catch (Exception e) {

            System.out.println(TAG + "연결안됨");
        }
    } // END OF CONSTRUCTOR

    // 클라이언트로 부터 메세지를 계속해서 받고 보내는 스레드
    class ClientInfo extends Thread {
        private Socket socket; // 클라이언트 소켓을 받아서 사용하는 변수.
        private PrintWriter writer; // 쓰기 버퍼.
        private BufferedReader reader; // 읽기 버퍼.

        public String clientId; // 클라이언트 아이디를 담는 변수.

        // 생성자에서 클라이언트 소켓을 내부 클래스 소켓에 담는다.
        public ClientInfo(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                String readerMsg = null;
                String[] parsReaderMsg;
                while ((readerMsg = reader.readLine()) != null) {
                    parsReaderMsg = readerMsg.split("&");
                    // Client Thread에서 동작하는 프로토콜.
                    protocolID(parsReaderMsg);
                    protocolChat(parsReaderMsg);
                    protocolEXIT(parsReaderMsg);
                    protocolDraw(parsReaderMsg);
                    protocolColor(parsReaderMsg);
                }
            } catch (Exception e) {
                System.out.println(TAG + "메세지 통신 실패.");
            }
        } // end of run





        // 나가기 버튼 프로토콜.
        // 나가기 버튼 누르면 서버메세지 출력, 해당 벡터 제거.
        private void protocolEXIT(String[] parsReaderMsg) {
            if (parsReaderMsg[0].equals("EXIT")) {
                for (int i = 0; i < vcClient.size(); i++) {
                    vcClient.get(i).writer.println("서버&" + clientId + " 님이 나가셨습니다.");
                }
                for (int i = 0; i < vcClient.size(); i++) {
                    if (vcClient.get(i).clientId.equals(parsReaderMsg[1])) {
                        vcClient.remove(i);
                    }
                }
            }
        }

        // ID 프로토콜.
        // 입력 받은 아이디를 저장 후 입장 서버메세지 출력.
        // 이 후 입장한 유저 목록을 위해 IDLIST 프로토콜로
        // 모든 클라이언트 한테 아이디 값을 전송.
        private void protocolID(String[] parsReaderMsg) {
            if (parsReaderMsg[0].equals("ID")) {
                clientId = parsReaderMsg[1];
                for (int i = 0; i < vcClient.size(); i++) {
                    vcClient.get(i).writer.println("서버&" + clientId + " 님이 들어왔습니다.");
                    vcClient.get(i).writer.println("ID&");

                    System.out.println("vcClient.get(" + i + ").clientId: " + vcClient.get(i).clientId);

                } // end of for
                for (int i = 0; i < vcClient.size(); i++) {
                    for (int j = 0; j < vcClient.size(); j++) {
                        vcClient.get(i).writer.println("IDLIST&[" + vcClient.get(j).clientId + "]");
                    }
                }
            }
        }


        // CHAT 프로토콜.
        // TextField 입력되는 값들은 모두 채팅으로 받음.
        private void protocolChat(String[] parsReaderMsg) {
            if (parsReaderMsg[0].equals("CHAT") && parsReaderMsg.length > 1) {
                for (int i = 0; i < vcClient.size(); i++) {
                    vcClient.get(i).writer.println("CHAT&[" + clientId + "]: " + parsReaderMsg[1]);
                }

            } else if (parsReaderMsg[0].equals("CHAT")) {
                for (int i = 0; i < vcClient.size(); i++) {
                    vcClient.get(i).writer.println("CHAT&[" + clientId + "] ");
                }

            }
        }

        private void protocolDraw(String[] parsReaderMsg) {
            if (parsReaderMsg[0].equals("DRAW")) {
                for(int i = 0; i < vcClient.size(); i++) {
                    if (vcClient.get(i) != this) {
                        vcClient.get(i).writer.println("DRAW&"+ parsReaderMsg[1]);
                    }
                }
            }
        }

        private void protocolColor(String[] parsReaderMsg) {
            if (parsReaderMsg[0].equals("COLOR")) {
                System.out.println("서버 칼라요청 메시지 들어옴");
                for(int i = 0; i < vcClient.size(); i++) {
                    if (vcClient.get(i) != this) {
                        vcClient.get(i).writer.println("COLOR&"+ parsReaderMsg[1]);
                        System.out.println("칼라변경 메시지 보냄");
                    }
                }
            }
        }


    } // end of class ClientInfo

    public static void main(String[] args) {
        new Server();
    }

}
