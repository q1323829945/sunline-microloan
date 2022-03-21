# k8s

``` 
注意事项:部署后
1.
 a.kubectl get pods -o wide [-n namespace] #找到对应的容器的name
 b.kubectl logs podName [-n namespace] #查看项目是否启动成功
 c.kubectl describe pod/podName [-n namespace] #查询详情
 d.  IP:           10.1.0.75
     Port:         8083/TCP   #记录ip与port
2.
 a.kubectl get svc [-n namespace] #找到对应服务的name
 b.kubectl describe svc/svcName [-n namespace] #查询详情
 c.Endpoints:     10.1.0.75:8083  #查看endpoints节点是否绑定容器成功（与容器的ip:port 一致）
3.
  远端请求ip为：宿主机IP:服务暴露端口  
```



```yaml
#控制器
apiVersion: v1
kind: ReplicationController
metadata:
  name: management-rc
  labels:
    name: management-rc
spec:
  replicas: 1 #集群数量
  selector:
    name: management #选择器 标记1 两个name一定要一样 用于绑定
  template:
    metadata:
      labels: 
        name: management
    spec:
      containers:
      - name: loan-management  #镜像名称
        image: sunline/management:v1.9  #docker镜像 name:version
        ports:
        - containerPort: 8083 #容器端口，同application.ymal 暴露的端口
---
#服务器
apiVersion: v1
kind: Service
metadata:
  name: loan-management-service
  labels: 
    name: loan-management-service
spec:
  type: NodePort #节点端口
  ports:
  - port: 8083 #服务器内部端口
    protocol: TCP
    targetPort: 8083 #容器暴露的端口
    name: http  
    nodePort: 30023 #对外暴露的端口 访问项目时  宿主机ip:nodePort
  selector:
    name: management #选择器 标记1  两个name一定要一样 用于绑定
        
        
```

