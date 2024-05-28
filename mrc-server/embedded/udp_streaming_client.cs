using System;
using System.Collections;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using UnityEngine;

public class Streaming : MonoBehaviour
{
    public string serverIP = "192.168.137.197";
    public int port = 8080;
    public GameObject Cube;
    private UdpClient client;
    private IPEndPoint serverEndPoint;
    private Thread receiveThread;
    private Texture2D tex;
    private Queue<Action> mainThreadActions = new Queue<Action>();

    void Start()
    {
        tex = new Texture2D(1920, 1080);
        serverEndPoint = new IPEndPoint(IPAddress.Parse(serverIP), port);
        client = new UdpClient();
        client.Connect(serverEndPoint);

        // 서버에게 클라이언트의 주소를 알려주기
        byte[] initialData = System.Text.Encoding.UTF8.GetBytes("Unity connected!");
        client.Send(initialData, initialData.Length);

        receiveThread = new Thread(ReceiveData);
        receiveThread.Start();
    }

    void Update()
    {
        lock (mainThreadActions)
        {
            while (mainThreadActions.Count > 0)
            {
                mainThreadActions.Dequeue().Invoke();
            }
        }

        if (tex != null)
        {
            Cube.GetComponent<Renderer>().material.mainTexture = tex;
        }
    }

    // private void ReceiveData()
    // {
    //     while (true)
    //     {
    //         try
    //         {
    //             // 이미지 길이 정보 수신
    //             byte[] lengthData = client.Receive(ref serverEndPoint);
    //             int imageLength = BitConverter.ToInt32(lengthData, 0);
    //             if (imageLength == 0) break;

    //             // 이미지 데이터 수신
    //             byte[] imageData = client.Receive(ref serverEndPoint);

    //             lock (mainThreadActions)
    //             {
    //                 mainThreadActions.Enqueue(() => tex.LoadImage(imageData));
    //             }
    //         }
    //         catch (Exception e)
    //         {
    //             Debug.LogError($"데이터 수신 오류: {e.Message}");
    //             break;
    //         }
    //     }
    // }
    private void ReceiveData()
    {
        while (true)
        {
            try
            {
                // 이미지 길이 정보 수신
                byte[] lengthData = ReceiveWithTimeout(client, serverEndPoint, 1000); // 1000ms = 1초
                if (lengthData == null || lengthData.Length < 4) continue; // 타임아웃이 발생하거나 길이 정보가 충분하지 않으면 다음 패킷을 기다림

                int imageLength = BitConverter.ToInt32(lengthData, 0);
                if (imageLength == 0) break;

                // 이미지 데이터 수신
                byte[] imageData = ReceiveWithTimeout(client, serverEndPoint, 1000); // 1초 동안 이미지 데이터를 기다림
                if (imageData == null || imageData.Length != imageLength) continue; // 타임아웃이 발생하거나 데이터가 충분하지 않으면 버림

                lock (mainThreadActions)
                {
                    mainThreadActions.Enqueue(() => tex.LoadImage(imageData));
                }
            }
            catch (Exception e)
            {
                Debug.LogError($"데이터 수신 오류: {e.Message}");
                break;
            }
        }
    }

    // 타임아웃을 적용한 데이터 수신 함수
    byte[] ReceiveWithTimeout(UdpClient client, IPEndPoint serverEndPoint, int timeout)
    {
        byte[] receivedData = null;
        client.Client.ReceiveTimeout = timeout; // 타임아웃 설정
        try
        {
            receivedData = client.Receive(ref serverEndPoint);
        }
        catch (SocketException e)
        {
            // 타임아웃 예외 처리
            if (e.SocketErrorCode == SocketError.TimedOut)
            {
                Debug.Log("데이터 수신 타임아웃");
            }
            else
            {
                throw;
            }
        }
        return receivedData;
    }


    void OnDestroy()
    {
        receiveThread?.Abort();
        client?.Close();
    }
}
