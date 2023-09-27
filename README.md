# async-api reference REPO
https://github.com/90rajibgarai/spring-boot-async

## for port forwarding:

kubectl port-forward service/mysql 3306:3306

kubectl port-forward <pod-name> <local-port>:<container-port>

## for connecting from mysql clinet on local machine to minikube mysql server:
kubectl run -it --rm --image=mysql:8.0 --restart=Never mysql-client -- mysql -h mysql -u root -p

-> password: "password", pass it to terminal

## minikube documentation:
https://minikube.sigs.k8s.io/docs/start/
https://kubernetes.io/docs/tutorials/hello-minikube/

## minikube dashboard command:
minikube dashboard
