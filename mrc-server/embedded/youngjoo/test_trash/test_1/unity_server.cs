using System.Collections;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using UnityEngine;
using System.Threading;
public class test1 : MonoBehaviour
{
    Thread mThread;
    // public string connectionIP = "127.0.0.1";
    private int connectionPort = 25001;
    IPAddress localAdd;
    TcpListener listener;
    TcpClient client;
    Vector3 receivedPos = Vector3.zero;
    bool running;
    private byte[] imageData;
    private bool imageReady = false;
    private void Update()
    {
        // if (imageReady)
        // {
        //     Texture2D receivedTexture = new Texture2D(1280, 720); //임의의 크기로 Texture2D 생성
        //     receivedTexture.LoadImage(imageData); //byte 배열을 이미지로 변환
        //     Debug.Log(receivedTexture);
        //     GetComponent<Renderer>().material.mainTexture = receivedTexture; //이미지를 GameObject의 Texture로 설정
        //     imageReady = false;
        // }
        transform.position = receivedPos; //assigning receivedPos in SendAndReceiveData()
    }
    private void Start()
    {
        ThreadStart ts = new ThreadStart(GetInfo);
        mThread = new Thread(ts);
        mThread.Start();
    }
    void GetInfo()
    {
        localAdd = IPAddress.Any;
        listener = new TcpListener(IPAddress.Any, connectionPort);
        listener.Start();
        client = listener.AcceptTcpClient();
        running = true;
        while (running)
        {
            SendAndReceiveData();
        }
        listener.Stop();
    }
    void SendAndReceiveData()
    {
        NetworkStream nwStream = client.GetStream();
        byte[] buffer = new byte[client.ReceiveBufferSize];
        //---receiving Data from the Host----
        int bytesRead = nwStream.Read(buffer, 0, client.ReceiveBufferSize); //Getting data in Bytes from Python
        string dataReceived = Encoding.UTF8.GetString(buffer, 0, bytesRead); //Converting byte data to string
        if (dataReceived != null)
        {
            print("received data!!");
            // print(bytesRead);

            if (bytesRead > 0)
            {
                print("received data!!");
                //---Using received data---
                // Texture2D receivedTexture = new Texture2D(2, 2); //임의의 크기로 Texture2D 생성
                // receivedTexture.LoadImage(buffer); //byte 배열을 이미지로 변환
                // GetComponent<Renderer>().material.mainTexture = receivedTexture; //이미지를 GameObject의 Texture로 설정
                // print("Image updated on the GameObject!");
                // //---Sending Data to Host----
                // byte[] myWriteBuffer = Encoding.ASCII.GetBytes("Image received and displayed!"); //Converting string to byte data
                // nwStream.Write(myWriteBuffer, 0, myWriteBuffer.Length); //Sending the data in Bytes to Python
                imageData = buffer;
                       
                Texture2D receivedTexture = new Texture2D(1280, 720); //임의의 크기로 Texture2D 생성
                receivedTexture.LoadImage(imageData); //byte 배열을 이미지로 변환
                Debug.Log(receivedTexture);
                GetComponent<Renderer>().material.mainTexture = receivedTexture; //이미지를 GameObject의 Texture로 설정
                // imageReady = false;
        
                // imageReady = true;
            }
            //---Using received data---
            // receivedPos = StringToVector3(dataReceived); //<-- assigning receivedPos value from Python
            // receivedPos = ReceiveImages(dataReceived); //<-- assigning receivedPos value from Python
            
            //---Sending Data to Host----
            // byte[] myWriteBuffer = Encoding.ASCII.GetBytes("Hey I got your message Python! Do You see this massage?"); //Converting string to byte data
            // nwStream.Write(myWriteBuffer, 0, myWriteBuffer.Length); //Sending the data in Bytes to Python
        }
    }

    
    // void SendAndReceiveData()
    // {
    //     NetworkStream nwStream = client.GetStream();
    //     byte[] buffer = new byte[client.ReceiveBufferSize];
    //     //---receiving Data from the Host----
    //     int bytesRead = nwStream.Read(buffer, 0, client.ReceiveBufferSize); //Getting data in Bytes from Python
    //     if (bytesRead > 0)
    //     {
    //         print("received data!!");
    //         //---Using received data---
    //         Texture2D receivedTexture = new Texture2D(2, 2); //임의의 크기로 Texture2D 생성
    //         receivedTexture.LoadImage(buffer); //byte 배열을 이미지로 변환
    //         GetComponent<Renderer>().material.mainTexture = receivedTexture; //이미지를 GameObject의 Texture로 설정
    //         print("Image updated on the GameObject!");
    //         //---Sending Data to Host----
    //         byte[] myWriteBuffer = Encoding.ASCII.GetBytes("Image received and displayed!"); //Converting string to byte data
    //         nwStream.Write(myWriteBuffer, 0, myWriteBuffer.Length); //Sending the data in Bytes to Python
    //     }
    // }


}