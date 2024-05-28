using System;
using System.Drawing;
using System.IO;
using System.Net.Sockets;
using System.Threading;
using System.Windows.Forms;

namespace PycameraStreaming
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            //네트워크 작업은 UI스레드를 블록하므로, 스레드에서 작업해줍니다.
            new Thread(ThreadTask).Start();
        }

        private void ThreadTask()
        {
            //서버에 접속하여 영상을 받아 올 클라이언트 소켓을 만들어서 접속해줍니다.
            Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            socket.Connect("192.168.137.197", 1234);
            while (true)
            {
                //파이썬과 다르게 C#에서는 스트림을 재활용할 수가 없어서, 반복문마다 새로 만들어줍니다.
                MemoryStream stream = new MemoryStream();

                //프레임의 크기(int형식, 4바이트)를 받기 위한 버퍼를 만들고, 크기를 받습니다.
                byte[] lBuf = new byte[4];
                socket.Receive(lBuf);

                //아래 사용할 BitConverter에서는 BigEndian을 사용하므로, Array.Reverse()함수를 이용해 뒤집어줍니다.
                Array.Reverse(lBuf);

                //프레임의 크기를 얻었습니다.
                int length = BitConverter.ToInt32(lBuf, 0);

                //프레임의 크기가 배열의 생성 가능 크기보다 클 수도 있어서,
                //작은 버퍼를 만든 후 스트림에 넣는 방법을 택합니다.
                //또한 이렇게 하면 바로 이미지로 바꿀 수도 있어 편리합니다.

                //len은 현재 받은 바이트 수, received는 전체 받은 바이트 수.
                byte[] buffer = new byte[1024];
                for (int len, received = 0; received < length;)
                {
                    received += (len = socket.Receive(buffer, Math.Min(1024, length - received), SocketFlags.None));
                    stream.Write(buffer, 0, len);
                }
                stream.Flush();

                //pictureBox에 이미지를 할당하는 것은 UI작업이므로, Invoke메서드를 이용하여 UI 스레드에서 작업해줍니다.
                this.Invoke((MethodInvoker)delegate
                {
                    //Image.FromStream() 메서드를 이용하면 스트림을 바로 이미지로 바꿀 수 있습니다.
                    pictureBox1.Image = Image.FromStream(stream);

                    //심심하니까 레이블에 이미지 크기를 표시해줍니다. 왠진 모르겠는데 대충 밝기랑 비례하는 것 같습니다.
                    label1.Text = "Image sie : " + length;
                });

                //스트림은 메모리를 잡아먹을 수 있으니까, 꼭 Dispose해줍니다. 까먹을 것 같으면 using블록을 사용하세요.
                stream.Dispose();
            }
        }
    }
}