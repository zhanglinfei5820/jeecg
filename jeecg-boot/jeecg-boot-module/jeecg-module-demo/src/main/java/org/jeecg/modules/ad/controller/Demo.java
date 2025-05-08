package org.jeecg.modules.ad.controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Demo {
    public static void main(String[] args) {
        // 请替换为你的 AccessKey ID
        String ossAccessKeyId = "445BC568E5QY56CBY9GD";
        // 请替换为你的 AccessKey Secret
        String ossAccessKeySecret = "bk5OlaNDlFvCu2KvNbRavBZdbXI68SWgKEDJJgen";
        // 请替换为你的 OSS Endpoint
        String ossEndpoint = "nanjing-3.zos.ctyun.cn";
        // 请替换为你的 OSS 存储桶名称
        String ossBucketName = "wyc-ad";
        // 要下载的对象键
        String objectKey = "1.jpg";
        // 本地保存文件的路径
        String localFilePath = "C:\\code\\1.jpg";
        System.out.println("-------------------------------------------------------");
        System.out.println("11111111111111111");
        System.out.println("-------------------------------------------------------");
        // 创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build("https://nanjing-3.zos.ctyun.cn:10443", ossAccessKeyId, ossAccessKeySecret);
//        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, ossAccessKeyId, ossAccessKeySecret);

        try {
            // 创建 GetObjectRequest 请求
            GetObjectRequest getObjectRequest = new GetObjectRequest(ossBucketName, objectKey);
            System.out.println("22222222222");
            // 下载文件
            OSSObject ossObject = ossClient.getObject(getObjectRequest);
            System.out.println("333333333333333333");
            // 获取文件输入流
            InputStream inputStream = ossObject.getObjectContent();
            System.out.println("444444444444444");
            // 创建本地文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(new File(localFilePath));
            System.out.println("555555555555");
            // 缓冲区大小
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("666666666666666");
            // 关闭流
            fileOutputStream.close();
            inputStream.close();

            System.out.println("文件下载成功，保存路径：" + localFilePath);
            /*// 创建 ListObjectsRequest 请求
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossBucketName);

            // 执行请求，获取存储桶中的对象列表
            ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
            System.out.println("-------------------------------------------------------");
            System.out.println("22222222222222222");
            System.out.println("-------------------------------------------------------");

            // 遍历对象列表并打印对象名称
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                System.out.println("-------------------------------------------------------");
                System.out.println("Object: " + objectSummary.getKey());
                System.out.println("-------------------------------------------------------");
            }
            System.out.println("成功访问存储桶！");*/
        } catch (OSSException e) {
            System.out.println("OSS 服务返回错误信息: 错误代码=" + e.getErrorCode() + ", 错误消息=" + e.getErrorMessage());
        } catch (ClientException e) {
            System.out.println("客户端发生错误: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("发生未知错误: " + e.getMessage());
        } finally {
            // 关闭 OSSClient
            ossClient.shutdown();
        }
    }

}
