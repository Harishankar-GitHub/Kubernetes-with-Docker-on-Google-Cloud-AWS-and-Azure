### :sparkles: Kubernetes with Docker on Google Cloud, AWS & Azure :sparkles:

##### :sparkles: Kubernetes and Docker with Microservices (Spring Boot + Java) on Google Cloud GKE, AWS EKS & Azure AKS :sparkles:

>- Kubernetes also called as K8S -> 'K' followed by 8 letters and an 'S'
>- Kubernetes Logo: Kubernetes in Greek means "Helmsman of a Ship"
>- Kubernetes is pronounced as KOO-BER-NET-EEZ

#### Kubernetes on Cloud:
- AKS (Azure Kubernetes Service)
- EKS (Amazon's Elastic Kubernetes Service)
- GKE (Google Kubernetes Engine)

Creating Google Cloud Account
-

- Create an account [here](https://cloud.google.com/).

> NOTE: Make sure you're inside My First Project.

- Search Kubernetes Engine and open it.
- Enable it.
- Click Create Cluster.

> NOTE: Make sure everything you do is in My First Project.

#### In Create Cluster page:

- Give a name for the cluster.
- Select Zone.
- Choose Master Version

#### After this, Go to Node pools

- Select Nodes and configure
- Choose the Machine Family
- Click Create

>Once Cluster is created, click to open it.

##### We can see many details such as,
- Kubernetes Version
- Total size
- Location of Master/Worker nodes etc.

>- Click Nodes tab to see the details of nodes.
>- And, that's it. Cluster is created!

---

Deploying First Spring Boot Application to Kubernetes Cluster
-
- To deploy an application to Kubernetes, we need to connect to Kubernetes Cluster.
- In Google Cloud, Top Right, Activate Cloud Shell.
- Open the terminal in a new window.
- If disconnected from Terminal, we can refresh the page to renew the connection.
- Now, back in Cluster page, Click Connect.
- Copy the GCloud command and paste it and click enter in Cloud Shell which is opened in another tab.

> NOTE: To set your Cloud Platform project in this session use “gcloud config set project [PROJECT_ID]”

##### Example command:
```
gcloud container clusters get-credentials harish-cluster --zone us-central1-a --project woven-arcadia-302411
```
>- Cluster Name: harish-cluster
>- Project ID: woven-arcadia-302411

By using the above command, we are connected to the cluster.
To execute commands against this cluster, we use kubectl commands.

> kubectl - Kube Controller

* **kubectl** is an awesome Kubernetes Command to interact with the cluster.
* **kubectl** would work with any Kubernetes Cluster irrespective of whether the cluster is in Local Machine or in any Data Center or in any Cloud.
* Once we are connected to the cluster, we can execute commands against any cluster.
- **kubectl** is already installed in Cloud Shell.
```
Command to check version: kubectl version
```

#### Command to deploy an application in Kubernetes Cluster (Run this in Cloud Shell)
```
kubectl create deployment hello-world-rest-api --image=in28min/hello-world-rest-api:0.0.1.RELEASE
```
>- Deployment name: hello-world-rest-api
>- Image name: in28min/hello-world-rest-api:0.0.1.RELEASE

* In the above command, the image is used from this [Docker Registry](https://hub.docker.com/u/in28min)
* Now the application is deployed in Kubernetes Cluster.

#### Command to expose the application to outside world:
```
kubectl expose deployment hello-world-rest-api --type=LoadBalancer --port=8080
```

> NOTE: Exposing it as a Load Balancer.

After exposing, to check the status of creation of the application,
we go back to the Google Cloud page -> Services and Ingress

In this page, we can see the status, etc.
We can find the Endpoints of the application.
```
Example Endpoint: http://35.188.59.125:8080/
```

##### Endpoints configured in hello-world-rest-api (Java Service):
```
http://35.188.59.125:8080/hello-world
http://35.188.59.125:8080/hello-world-bean
```
We can hit the Endpoint in the browser/Postman to see the response.

---

Saving the Free Credits in Google Cloud:
-
> In the cloud, it is best practice to delete the resources when you are not using them.
> If you do not want to delete and create a new cluster every time, you can reduce the cluster size to zero.
> After finishing your work you can reduce cluster node size to zero.
##### Resizing the number of nodes to 0
```
gcloud container clusters resize --zone name_of_zone name_of_your_cluster --num-nodes=0
```
##### When you are ready to start again, increase the number of nodes
```
gcloud container clusters resize --zone name_of_zone name_of_your_cluster --num-nodes=3
```
##### Example:
```
Set the Project ID first: gcloud config set project woven-arcadia-302411
```
```
gcloud container clusters resize --zone us-central1-a harish-cluster --num-nodes=0
```
- It will ask Do you want to continue (Y/n)? Type Y
- And that's it! We resized the cluster to 0.

##### When ready to start again, increase the number of nodes.
```
gcloud container clusters resize --zone us-central1-a harish-cluster --num-nodes=3
```
---

- **To see the events:** kubectl get events
- **To get all pods:** kubectl get pods or kubectl get pod
- **To get all replicasets:** kubectl get replicaset or kubectl get replicasets or kubectl get rs
- **To get all deployments:** kubectl get deployment
- **To get all services:** kubectl get service

#### Kubernetes uses Single Responsibility Principle
- 1 concept - 1 responsibility
- Each of these, a pod, replicaset, deployment, service have a very important role to play.

##### When we executed the command:
- kubectl create deployment... -> It created deployment, replicaset, pod
- kubectl expose deployment... -> It created a service

Each of these concepts have 1 important responsibility that they do really well.
As a combination, they provide the important responsibilities of Kubernetes which are:
- To manage workloads
- To provide external access to workloads
- To enable scaling
- To enable zero downtime deployments

> To clear Cloud Shell Console: clear

---

Understanding Pods in Kubernetes:
-
- To see the IP Address and other details of the pod: kubectl get pods -o wide
-o wide will add few more details to the kubectl get pods command.

* Within the same pod, the containers can talk to each other using localhost.

- Command that tells what a pod is: kubectl explain pods
Scroll -> Shift+UP/DOWN or Shift+PageUp/PageDown

**Describe command:** kubectl describe pod podId
```
Example: kubectl describe pod hello-world-rest-api-f5c8584d4-kmm6s
```
This gives specific details like IP, Node, Status, Namespace etc. of that particular pod.

---

Understanding ReplicaSets in Kubernetes
-
```
To get the replicasets: kubectl get rs
```
ReplicaSets ensure that a specific number of pods are running at all time.
```
To delete a pod: kubectl delete pod podId
Example command: kubectl delete pod hello-world-rest-api-f5c8584d4-kmm6s
```
* Once we delete a pod, another pod will be up and running.
* This is because of ReplicaSets. ReplicaSets always keep monitoring the pods
and if there are less number of pods than needed, then it creates the pods.

##### To increase number of pods in replicaset
```
kubectl scale deployment hello-world-rest-api --replicas=3
```

Now if we hit the endpoints of the application - http://35.188.59.125:8080/hello-world
the load gets distributed and we get the response from 3 different containers/instances.
```
To see what happened in the background: kubectl get events
```
> NOTE: The events displayed are not in sorted order.
```
To sort: kubectl get events --sort-by=.metadata.creationTimestamp
```
>The above command sorts the events based on CreationTimestamp.
>Path of CreationTimestamp: .metadata.creationTimestamp
```
To explain ReplicaSets: kubectl explain rs
```
---

Understanding Deployment in Kubernetes:
-
##### The need:

- We have V1 version of an application deployed.
- We need to deploy V2 version.
- We need Zero Downtime deployment.
```
To deploy a new version: kubectl set image deployment deployment-name container-name=new-image-name
```
```
kubectl set image deployment hello-world-rest-api hello-world-rest-api=DUMMY_IMAGE:TEST
```
- In the above command, we have given a wrong image name.
- If we check kubectl get rs or kubectl get pods, the wrong pod will not be ready as the image is incorrect.
- So, the previous version of application will be up and running.
- The status will be InvalidImageName.
- To see the details of the pod: kubectl describe Id_Of_Error_Pod

##### Deploying a new version with proper command:
```
kubectl set image deployment hello-world-rest-api hello-world-rest-api=in28min/hello-world-rest-api:0.0.2.RELEASE
```
Now,

- We have 3 instances of V1 running.
- A new pod with V2 application is launched up (1 V2 pod is up)
- 1 pod of V1 is down (2 V1 pods remaining)
- Another V2 pod is up (2 V2 pods are up)
- 1 pod of V1 is down (1 V1 pods remaining)
- Another V2 pod is up (3 V2 pods are up)
- 1 pod of V1 is down (All V1 pods are down)

> **1 new pod us brought up at a time and then 1 old is brought down.
The deployment happens in this way.**

---

Overview of Kubernetes concepts: Pods, ReplicaSets & Deployment
-
##### Pod:
- A pod is a wrapper for a set of containers.
- It has IP address, lables, annotations, etc.

##### ReplicaSets:
- A replicaset ensures that a specific number of pods are always running.
- Even if an instance is killed, replicaset would observe and bring up another pod.
- A replicaset is always tied with a specific release version. If updated version of application is
deployed, a new replicaset is created.

##### Deployment:
- A deployment ensures a switch from V1 to V2 (Release upgrade) happens without any issue.
- It plays a key role in offering Zero Downtime deployments.

##### Deployment strategies:
- Default strategy: Rolling Updates
- 50% traffic to V1 and 50% traffic to V2
- 1 instance of V2, Test it, once it is fine, reduce the number of instances of V1 and so on till
V1 instances comes to 0.

---

Understanding Services in Kubernetes:
-
> **kubectl delete command** can be used to delete any of the Kubernetes Resources such as
a pod, a replicaset, a deployment, etc.

- When we delete a pod, another one comes up.
- Each time, the new pod has a new IP.
- But the URL of the application continues to work as usual.
- This is because of the Service.

* In Kubernetes world, a pod is a **throw-away unit**.
* It might go down, new pods might come up.
* There might be several new pods which are created and the old pods completely go away.

- But we don't want the users to use a different URL everytime.
- That's the role of a Service.
- The role of a Service is to provide an always available external interface to the applications which are running inside the pods.

* A Service basically allows the application to receive traffic through a permanent lifetime IP Address.
* That IP Address is created when we expose a deployment.

- To view the Load Balancer that is created for this application,
Search Load Balancer in the Google Cloud search bar and select Load Balancing (Network Services).

Click on the load balancer to view the Frontend and Backend details.
##### Frontend:
- IP Address
- Network Tier
- Protocol

##### Backend:
- Instances
- Zone

>* This is the Load Balancer that is getting updated when we CREATE/DELETE pods etc in the Backend.
>* However the **Frontend remains the same (Same URL, PORT)**.

>- The great thing about Kubernetes is the fact that it provides excellent integration with different Cloud Providers' specific Load Balancers.

>* We were using a specific type of service called Load Balancer.
>* And Google Cloud Platform Load Balancer is created for us.
>* This shows how well Kubernetes is integrated with Google Cloud Platform to create a Load Balancer.

>- If we are using AWS or Azure, then that Cloud Provider specific Load Balancers would've been created as a service.
```
Command to get services: kubectl get services
This command would list the service that are running right now.
```

In the output of the **kubectl get services** command:
TYPE: LoadBalancer, ClusterIP

* LoadBalancer is the application which we deployed.
* Kubernetes service is running as ClusterIP service.
* ClusterIP service can be accessed only from inside the Cluster.
* That's why **ClusterIP doesn't have an External-IP where as LoadBalancer has one**.

---

Quick review of GKE on Google Cloud Console:
-
In Kubernetes Engine page -> Workloads -> Click on Application-Name

We have options for
* Refresh
* Edit
* Delete
* Actions (Autoscale, Expose, Rolling update, Scale)

In Workloads page -> Revision History
We can view the history of deployments that we made.

> NOTE: Most of the things we did using the kubectl commands can be done from UI as well.
> But instead of depending on Cloud Provider specific UI/Console, it is better to use
> kubectl commands as it is a Cloud Neutral way of interacting with Kubernetes.

---

Understanding Kubernetes Architecture - Master Node and Worker Nodes
-
* Master Node(s) -> **Manage Clusters**
* Worker Node(s) -> **Run applications**

### Master Node:
##### A Master Node has:
- API Server (kube-apiserver)
- Distributed Database (etcd)
- Scheduler (kube-scheduler)
- Controller Manager (kube-controller-manager)

#### Distributed Database (etcd):
>* Most important component that is running as part of Master Node is called etcd.
>* That's the Distributed Database.
>* All the configuration changes that we make, all deployments that we create, all scaling operations that
we perform etc. are stored in a Distributed Database.

>- When we are executing those commands, we are setting the desired state for Kubernetes.
>- So, all the Kubernetes resources like deployment, services, all the configuration that we make is stored
into the Distributed Database.

>* The great thing about this database is it is Distributed!

>- Typically, it is recommended to have about 3 to 5 replicas of this database so that Kubernetes
Cluster state is not lost.

#### API Server:
>* The 2nd important component inside the Master Node is called API Server.
>* If I make a change using kubectl commands or using Google Cloud Console UI, it is submitted
to this API Server and processed here.

#### Scheduler & Controller Manager:
The other 2 important components are Scheduler and Controller Manager.

>- The Scheduler is responsible for scheduling the pods on to the nodes.
>- In Kubernetes Cluster, we have several nodes. When we create a new pod, we have to decide which node the
pod has to be scheduled on to.
>- The decision might be based on how much memory is available, how much CPU is available, are there
any port conflicts, and a lot of such factors.
>- So, Scheduler considers all these factors and schedules the pods on to the appropriate nodes.

>* The Controller Manager manages the overall health of the cluster.
>* Wherever we execute the kubectl commands, we are updating the desired state.
>* The kubectl manager makes sure that whatever the desired state that we have/set, all those changes
need to be executed into the cluster and the controller manager is responsible for that.
>* It makes sure the actual state of the Kubernetes Cluster matches the desired state.

> The important thing about Master Node is, the user applications (which we developers deploy) like
hello-world-rest-api won't be scheduled on to the Master Node. All the user applications would be running
in pods inside the Worker Node or Node.

---

### Worker Node or Node:

##### A Worker Node has:
- Pods (Multiple pods running containers)
- Container Runtime (CRI - docker, rkt etc)
- Networking Component (kube-proxy)
- Node Agent (kubelet)

>* One of the important components of the Worker Node is the applications that we need to run.
These applications run inside a pods.
On a single node, we can have several pods running.

>- The job of a Node Agent (kubelet) is to make sure that it monitors what's happening on the node
and communicates it back to the Master Node.
So, if a pod goes down, Worker Node reports to the Controller Manager of Master Node.

>* Earlier, we created a deployment and exposed the deployment as a service.
That is possible through the Networking Component.
It helps us in exposing services around our nodes and pods.

>- We need to run containers inside the pods. And this needs Container Runtime.
The most frequently used Container Runtime is Docker.

> NOTE: We can use Kubernetes with any OCI (Open Container Interface) runtime spec implementations.

---

> NOTE: If Master Node or any of the services in the Master Node goes down, the application will be up
and running.
When we try to hit an application, Master Node is not getting involved at all.
However, when the Master Node is down, we won't be able to make any changes to them, but the existing
applications would continue to run.
```
Command to get status of components: kubectl get componentstatuses
```
[Looks like the above command is deprecated](https://github.com/kubernetes/enhancements/issues/553)

---

Understand Google Cloud Regions and Zones
-
[Regions and Zones](https://cloud.google.com/about/locations)

Need for multiple regions:
- To achieve low latency
- Availability
- Legal requirements (Storing data of a partifular country in same country)

Zones are physically isolated data centers
A region might have multiple Zones.

>* Google Cloud calls it Zones.
>* AWS calls it Availability Zones.

Using Kubernetes and Docker with Spring Boot Hello World Rest API
-
### Steps to deploy Spring Boot Hello World Rest API to Kubernetes
>Install Docker Desktop *(Windows 10 Pro)* or Docker Toolbox *(Windows 10 Home)* in local machine.
1. We have a service **Hello World Rest API**
2. We have a **Docker File** in project folder & **Dockerfile Maven Plugin** in pom.xml. Make sure the Repository name and the Image name is properly configured in the plugin in pom.xml.
3. To build an image: Right click on project -> Run As -> Maven Build -> Type "**clean install -DskipTests**" in Goals textbox and click Run.
4. Command to run the docker image (Run this command in Docker Terminal)
```
docker run -p 8080:8080 rhsb/hello-world-rest-api-kubernetes:0.0.4-SNAPSHOT
```
>Explanation: docker run -p portWeNeedToExpose:portWhereApplicationIsRunning dockerRepositoryName/projectName:tagName
5. Testing the service
>http://192.168.99.100:8080/hello-world or http://localhost:8080/hello-world
6. Pushing the Docker Image to [Docker Hub](https://hub.docker.com/)
>Create an account in [Docker Hub](https://hub.docker.com/)
>Run the below commands in Docker Terminal
```
docker login
```
>Enter the username and password (Prompted if not logged in previously).
```
docker push rhsb/hello-world-rest-api-kubernetes:0.0.4-SNAPSHOT
```

*That's it! The Docker Image is successfully pushed to [Docker Hub](https://hub.docker.com/)*

7. Install [GCloud](https://cloud.google.com/sdk/docs/install#windows)
>* Google Cloud can also be installed as a Docker Image from [Google Cloud SDK - Docker Hub](https://hub.docker.com/r/google/cloud-sdk) or [Google Cloud SDK - Google Container Registry](https://cloud.google.com/sdk/docs/downloads-docker)
>* ***Google Cloud SDK Shell*** and ***Cloud Tools for PowerShell*** will be installed.
>* When logging in **next** time, Open *Google Cloud SDK Shell* or *Cloud Tools for PowerShell* in local, type `gcloud auth login`
>* To set project id, *gcloud config set project PROJECT_ID*
8. Install [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/#install-kubectl-on-windows)
>There are many ways to install in Windows. We can choose whichever is comfortable.

>To check the version
```
kubectl version (In Google Cloud SDK Shell or Docker Terminal)
```
>To ensure the version you installed is up-to-date
```
kubectl version --client (In Google Cloud SDK Shell or Docker Terminal)
```
>In Google Cloud SDK Shell, run the below command to connect to the cluster which is in Google Cloud. This command can be taken from the **Google Cloud Console -> Cluster Page -> Our Cluster -> Connect**.
```
gcloud container clusters get-credentials harish-cluster --zone us-central1-a --project woven-arcadia-302411
```
>After connecting to the cluster from Google Cloud SDK Shell, check the version again. `kubectl version`
>***We can see Client Version as well as Server Version***

>*The interesting thing about connecting from Local (Google Cloud SDK Shell) is the fact that
it gives a **lot more flexibility when compared to the Cloud Shell that is in Google Cloud UI**.
We can create files more easily in Local and put them in version control much more easily.*

9. Deploying Hello World Rest API to Kubernetes
>- We already deployed this service from Google Cloud Console itself.
>- Hence, updating the existing deployment with a new version of the service using the below command.
```
kubectl set image deployment hello-world-rest-api hello-world-rest-api=rhsb/hello-world-rest-api-kubernetes:0.0.4-SNAPSHOT
```
>Explanation for the above command: kubectl set image deployment deployment-name container-name=new-image-name

##### To view the history of deployment
```
kubectl rollout history deployment deployment-name
```
```
kubectl rollout history deployment hello-world-rest-api
```
>By checking the rollout history, we can see the revisions, but the CHANGE-CAUSE will be <none>.
To set the CHANGE-CAUSE, we can add ***--record*** in the command while creating/updating a deployment.
By adding --record, the **command used to update the deployment will be set to CHANGE-CAUSE**.
```
kubectl set image deployment deployment-name container-name=new-image-name --record
```
```
kubectl set image deployment hello-world-rest-api hello-world-rest-api=rhsb/hello-world-rest-api-kubernetes:0.0.4-SNAPSHOT --record
```
##### To check the status of deployment
```
kubectl rollout status deployment deployment-name
```
```
kubectl rollout status deployment hello-world-rest-api
```
##### To undo the deployment
```
kubectl rollout undo deployment deployment-name --to-revision=revisionNumber
```
```
kubectl rollout undo deployment hello-world-rest-api --to-revision=3
```
>We can also pause/unpause the deployments.
##### To see the Pod logs
```
kubectl logs podId
```
```
kubectl logs hello-world-rest-api-58dc9d7fcc-2fw9n
```
```
To follow the logs: kubectl logs podId -f
```
```
kubectl logs hello-world-rest-api-58dc9d7fcc-2fw9n -f
```
##### Watch command

###### :worried::anguished: ***Watch command is not supported on Windows*** :worried::anguished:

>- This command is used to execute a specific url in specific interval. For example: Every 2 seconds.
>- To execute this command in Local, we have to install watch for our respective OS.
>- Cloud Shell that is in **Google Cloud Console UI has watch installed already**
```
watch curl URL
```
```
watch curl http://35.188.59.125:8080/hello-world
```
* Specifying the interval in the command. The below command will hit the service every 0.1 seconds.
```
watch -n 0.1 curl http://35.188.59.125:8080/hello-world
```
>We can watch logs `kubectl logs podId` and observe the logs when *watch command* continuously hits the URL.

Generating Kubernetes YAML Configuration for Deployment and Service
-
* Till now, we were executing kubectl commands to create deployment, check logs and many more.
* However Kubernetes supports a YAML format to define deployments, services, etc.

>In real world projects, we will be using YAML files to create deployments, services, etc.

##### To view the deployment
```
kubectl get deployment deployment-name
```
```
kubectl get deployment hello-world-rest-api
```
To view some more details
```
kubectl get deployment deployment-name -o wide
```
```
kubectl get deployment hello-world-rest-api -o wide
```

##### To view the YAML file from the deployment
```
kubectl get deployment deployment-name -o yaml
```
```
kubectl get deployment hello-world-rest-api -o yaml
```
>This YAML file will have details such as *label name, image name, replicas, status* etc.

##### Copying the YAML file to a Local file
>- Navigate to the project folder and run the below commands so that the YAML files will be created inside the project.
>- If navigation not working from ***Google Cloud SDK Shell***, try navigating from ***Cloud Tools for PowerShell***
>- Both *Google Cloud SDK Shell* and *Cloud Tools for PowerShell* will be installed while installing Google Cloud.
```
kubectl get deployment deployment-name -o yaml > deployment.yaml
```
```
kubectl get deployment hello-world-rest-api -o yaml > deployment.yaml
```
##### Getting the information of expose command
>- `kubectl get deployment deployment-name -o yaml` command is used to view the information **when we create a deployment**.
>- `kubectl get service deployment-name -o yaml` command is used to view the information **after we expose the deployment**.

>- Navigate to the project folder and run the below commands so that the YAML files will be created inside the project.
>- If navigation not working from ***Google Cloud SDK Shell***, try navigating from ***Cloud Tools for PowerShell***
>- Both *Google Cloud SDK Shell* and *Cloud Tools for PowerShell* will be installed while installing Google Cloud.
```
kubectl get service service-name -o yaml
```
```
kubectl get service hello-world-rest-api -o yaml
```
##### Copying the YAML file to a Local fileCopying it to a local file
```
kubectl get service hello-world-rest-api -o yaml > service.yaml
```
##### Updating the deployment by updating the YAML files.
```
kubectl apply -f yaml-file-name.yaml
```
```
kubectl apply -f deployment.yaml
```
- For example, in deployment.yaml file, we changed replicas from 3 to 2.
- By running the above command, the deployment will be updated and there will be only 2 replicas and not 3.
- Instead of running kubectl commands directly to update, we can change the configuration in YAML files.

---

Improving Kubernetes YAML Configuration
-

- Now, we have *deployment.yaml and service.yaml* files in the project folder.
- We can ***combine both the files*** into a single file.

>- Copy the contents of *service.yaml* file and paste it in *deployment.yaml*
>- *service.yaml* file can be deleted.

- Removing few configurations from YAML files that aren't needed
>- I have added ***deployment_backup.yaml*** and ***service_backup.yaml*** for reference (If needed)
>- Backups are also available in ***backup*** folder in the project.

##### To delete a resource using label name
> The label name will be configured in the YAML file.
```
kubectl delete all -l app=hello-world-rest-api
```
##### Creating a deployment from Local after deleting the existing deployment
- Now, that we have combined deployment.yaml and service.yaml into 1 file (deployment.yaml),
we can execute the below command
> Make sure we are inside project folder while executing the commands.

>* NOTE: *After combining ***deployment.yaml*** and ***service.yaml***, a backup of the file is available under Backup folder (***deployment-01-after-cleanup.yaml***)*
```
kubectl apply -f deployment.yaml
```

##### To get information about all the resources (Pods, Service, Deployment, Replicaset)
```
kubectl get all
```
##### Hitting the service from Google Cloud Shell SDK or Cloud Tools for PowerShell from Local machine
```
curl 35.188.59.125:8080/hello-world
```

---

Playing with Declarative Configuration for Kubernetes
-
##### Understanding Kubernetes YAML Configuration - Labels and Selectors
+ On a high level, 4 important sections of a YAML file are
	- ApiVersion
	- Kind
	- MetaData
	- Spec

* `apiVersion:` will have the version details
* `kind:` deployment or service etc.
* `metadata:` label, name, namespace etc.
* `spec:` definitions of a deployment
> One of the important definition inside spec is *definition of pod*

##### Reducing release downtime with minReadySeconds
>* We can add minReadySeconds: 45 in the YAML file under *spec* section.
>* The application will take few seconds to start. That's why adding 45 seconds.

> Adding minReadySeconds is a quick fix. The perfect solution would be to add readiness and liveliness
probes.

* Command to see the difference between existing YAML file and a modified YAML file
###### :worried::anguished: ***diff command is not supported on Windows*** :worried::anguished:
```
kubectl diff -f deployment.yaml
```

##### Understanding Replica Sets in Depth - Using Kubernetes YAML Config
* Replicasets can live on their own without a deployment.
* We can directly attach the pods created by a replicaset to the service

> Changing `kind: Deployment` to `kind: ReplicaSet` in deployment.yaml file.

* **Deployment** is *responsible for new releases*.
* **ReplicaSet** is *only responsible for ensuring that specific pods are running*.
	+ Replicaset does not know anything about Strategy. Hence commenting it in YAML file.

To create a service, we don't need deployments.
All we need is Pods.
The service is directly attached to the pods using labels of the pods.
And the ReplicaSet is attached to the pod.

>* When we change the image name or version and run `kubectl apply -f deployment.yaml` command,
the updated pods won't start/run.
>* This is because, ReplicaSet just ensures specific pods are up and running.
>* When we delete a pod, then new pod will come up which will contain the updated image.

***That's why we use deployments.*** When we create a deployment, it creates replicaset and pods.

> NOTE: *The backup of the ***deployment.yaml*** file is under Backup folder (***deployment-02-using-replica-set.yaml***).*

##### Configure Multiple Kubernetes Deployments with One Service

- Modified the **deployment.yaml**: Added 1 more deployment configuration (Now we have 2 deployment configuration)
- Service configuration remain unchanged.

After this, run
```
kubectl apply -f deployment.yaml
```

>* 2 deployments will be created
>* 2 pods of V1 and 2 pods of V2 will be up and running.

- Run `kubectl get all'  to see what resources are newly created.
- Run `kubectl get all -o wide'  to see **more details*	* what resources are newly created.

>* Now if we hit the service, the load will be distributed to V1 and V2 pods.

###### How can we send the load only to a specific deployment ?

- As of now, the configuration of service in the deployment.yaml file has only 1 label (*app: hello-world-rest-api*)
- It doesn't have *version: v1* or *version: v2*.
- If we specify v1 or v2 in the ***selector section*** of the service configuration, **the load will be sent only to that specific pods**.

Using Kubernetes and Docker with Java Spring Boot Todo Web Application
-
### Steps to deploy Spring Boot Todo Web Application to Kubernetes
1. We have a service **Spring Boot Todo Web Application**
2. We have a **Docker File** in project folder & **Dockerfile Maven Plugin** in pom.xml. Make sure the Repository name and the Image name is properly configured in the plugin in pom.xml.
3. To build an image: Right click on project -> Run As -> Maven Build -> Type "**clean package**" in Goals textbox and click Run.
4. Now, we have an ***image*** and a ***.war*** file generated.
5. Created a ***deployment.yaml*** file similar to ***hello-world-rest-api*** service and modified accordingly to deploy ***Todo Web Application***.
>-   Backup of deployment.yaml file is also available in  _**backup**_  folder in the project.

6. Push the image to [Docker Hub](https://hub.docker.com/)
7. After this, we can deploy using
```
kubectl apply -f deployment.yaml
```
### :innocent::wink: The application is up and running in Kubernetes! :innocent::wink:

Playing with Kubernetes Commands - Top Node and Pod
-
- To get all details
```
kubectl get all
```
- To get all namespaces
```
kubectl get pods --all-namespaces
```
- Passing a filter based on a label on `kubectl get` command
```
kubectl get pods -l app=todo-web-application-h2
```
```
kubectl get pods -l app=todo-web-application-h2 --all-namespaces
```
- To get all namespaces of services
```
kubectl get services --all-namespaces
```
- Sorting based on a column
> The output of `kubectl get services --all-namespaces` will have columns such as NAMESPACE, NAME, TYPE, PORTS etc.
If we need to sort the output based on TYPE, we can use the below command.
```
kubectl get services --all-namespaces --sort-by=.spec.type
```
>- `.spec.type` is formed based on deployment.yaml file.
>- In deployment.yaml file, inside *spec:* section *type:* is defined. So, `--sort-by=.spec.type`
- To see cluster info
```
kubectl cluster-info
```
> To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
- To see top nodes
```
kubectl top node
```
> This command lists down the nodes based on the CPU and Memory utilization
- To see the top pods
```
kubectl top pod
```
> This command lists down the pods based on the CPU and Memory utilization

##### Few shortcuts

- `kubectl get services` -> `kubectl get svc`
- `kubectl get events` -> `kubectl get ev`
- `kubectl get replicasets` -> `kubectl get rs`
- `kubectl get namespaces` -> `kubectl get ns`
- `kubectl get nodes` -> `kubectl get no`
- `kubectl get pods` -> `kubectl get po`

# :wink: [Kubernetes (kubectl) Cheet Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/) :wink:

Using Kubernetes and Docker with Java Todo Web Application using MySQL
-
### Steps to deploy Spring Boot Todo Web Application to Kubernetes
1. We have a service **Spring Boot Todo Web Application using MySQL**
2. This service uses MySQL Database. We can use MySQL image from [Docker Hub](https://hub.docker.com/_/mysql) instead of manually installing and configuring MySQL Database.
- Command to pull, configure and run MySQL Database in our local.
```
docker run -d -e MYSQL_ROOT_PASSWORD=dummy -e MYSQL_DATABASE=todos -e MYSQL_USER=todos-user -e MYSQL_PASSWORD=dummy --name mysql -p 3306:3306 mysql:5.7
```
3. We need [MySql Shell](https://dev.mysql.com/downloads/shell/) to connect to the MySQL Database that is running as a Docker Image.
>* While installing if we get an error like *vcruntime140_1.dll* is missing,
	we need to download the file from google and keep it in mysqlsh.exe path.
>- Once MySql Shell is downloaded and installed, double click on mysqlsh.exe file to open MySql Shell.
>* *To connect to database*, type `\connect todos-user@localhost:3306` or `\connect todos-user@192.168.99.100:3306`
```
\connect MYSQL_USER@localhost:PortNumber of MySql which is running in docker.
```
> **Note:** Default port of MySql is 3306
- After connecting MySql Docker Image from MySql Shell, we can use the below commands to work.
	+ `\sql` - Switching to SQL mode
	+ `use databaseName` - To work on a particular database
	+ After this, we can execute queries like this. `select * from tableName;`

* Now, if we start the application, it will be connected to the MySQL Database.
* We can execute few queries to check if it works properly.
4. We have a **Docker File** in project folder & **Dockerfile Maven Plugin** in pom.xml. Make sure the Repository name and the Image name is properly configured in the plugin in pom.xml.
5. To build an image: Right click on project -> Run As -> Maven Build -> Type "**clean install**" in Goals textbox and click Run.
6. Now, we have an ***image*** and a ***.war*** file generated.
7. We need to connect and run the ***Web Application Docker Image*** with the ***MySQL Docker Image***.
```
docker run -d -p 8080:8080 --link=mysql rhsb/todo-web-application-mysql-kubernetes:0.0.1-SNAPSHOT
```
>- `--link=mysql` used to connect Web Application with MySql
>- **Note:** `--link` is deprecated.

##### A better alternative to --link is to launch both the applications into a custom docker network
* Commands:
```
docker network ls
docker network create web-application-mysql-network
docker inspect web-application-mysql-network
docker run --detach --env MYSQL_ROOT_PASSWORD=dummy --env MYSQL_USER=todos-user --env MYSQL_PASSWORD=dummy --env MYSQL_DATABASE=todos --name mysql --publish 3306:3306 --network=web-application-mysql-network mysql:5.7
docker inspect web-application-mysql-network
```
8. Instead of running multiple commands to run multiple applications/databases etc., we can combine all the configurations into 1 file (***docker-compose.yml***).
##### Docker Compose
- Docker Compose is used to ***launch up multiple containers*** at a time/simultaneously. It is ***also used to associate resources/volumes, network etc.***
-   Documentation:  [Docker Compose](https://docs.docker.com/compose/)
- To check the docker-compose version ***`docker-compose -version`***
- Docker Compose is preinstalled in Docker Desktop. If not available, it can be installed from [here](https://docs.docker.com/compose/)
9. Created a  ***docker-compose.yaml*** which has configurations to run Web Application and the Database.
* To run *docker-compose.yaml*
	+ cd to the project folder where *docker-compose.yaml* is present.
	+ Run ***`docker-compose up`*** in Docker Terminal
10. Now that we have created *docker-compose.yml* file, we need to create *deployment.yaml* file to deploy the application to Kubernetes.
* We can convert *docker-compose.yml* file to *deployment.yaml* file using a tool called [Kompose](https://kompose.io/installation/)
* Easy way to install [***Kompose***](https://kompose.io/installation/) is via [***Chocolatey***](https://chocolatey.org/install)
	+ In Windows PowerShell *(Run as Administrator)* run the command to install [Chocolatey](https://chocolatey.org/install).
	+ Once Chocolatey is installed, we can install Kompose from this [link](https://kompose.io/installation/)
	+ There are many ways to install Kompose. I installed using Chocolatey.
	+ The command is `choco install kubernetes-kompose`

##### Converting docker-compose.yml to Kubernetes Deployment YAML files
- In Windows PowerShell, cd to the project folder where docker-compose.yml file is present.
- Run `kompose convert`
- Kubernetes Deployment YAML files will be created!
11. We can modify/remove few configurations from the Kubernetes YAML files.
12. Now we are ready to deploy this application to Kubernetes.
- Deploying MySQL to Kubernetes
```
kubectl apply -f mysql-database-data-volume-persistentvolumeclaim.yaml,mysql-deployment.yaml,mysql-service.yaml
```
> After deploying, we can check the details using `kubectl get all` and `kubectl get service --watch`
- Deploying Web Application to Kubernetes
```
kubectl apply -f todo-web-application-deployment.yaml,todo-web-application-service.yaml
```
> After deploying, we can check the details using `kubectl get all` and `kubectl get service --watch`

### :innocent::wink: The application is up and running in Kubernetes! :innocent::wink:

Understanding Persistent Storage with Kubernetes
-
- To get Persistent Volume
```
kubectl get pv
```
- To get Persistent Volume Claim
```
kubectl get pvc
```
- In terms of Kubernetes Cluster, ***any kind of storage is called as Volume***.
- Different Cloud Providers provide different Storage Solutions such as ***AWS Elastic Block Storage***, ***GCE Persistent Disk (Google Compute Engine Persistent Disk)*** etc.
- A ***Persistent Volume*** is how we map an external storage to our cluster.
- A ***Persistent Volume Claim*** is how a pod can ask for the Persistent Volume.

Using Config Maps for Centralized Configuration
-
##### Creating ConfigMap
- Creating a ConfigMap with the values in Todo Web Application with MySQL
```
kubectl create configmap todo-web-application-config --from-literal=RDS_DB_NAME=todos
```
- A ConfigMap is created  which has *RDS_DB_NAME* and its value.

##### Viewing the ConfigMap
```
kubectl get configmap
kubectl get configmap todo-web-application-config
kubectl get configmap/todo-web-application-config
```
##### Describe ConfigMap
```
kubectl describe configmap
kubectl describe configmap todo-web-application-config
kubectl describe configmap/todo-web-application-config
```
##### Deleting ConfigMap
```
kubectl delete configmap/todo-web-application-config
kubectl delete configmap todo-web-application-config
```
- Now we have created a ConfigMap. We can modify the deployment.yaml files to use the value from ConfigMap. ***(Refer deployment.yaml files for code changes)***
- After modifying deployment.yaml files, we can use `kubectl apply -f todo-web-application-deployment.yaml` to update the deployment.

##### Adding all configurations to ConfigMap
- Instead of adding variables one by one to the ConfigMap, we can open the ConfigMap and edit there.
> ***Note: If any issues while editing the ConfigMap in local machine, we can open Cloud Shell from Google Cloud Console UI and edit from there !!*** :wink:

* Once the ConfigMap is edited, we can restart the pods by deleting the pods (By deleting, new pods will come up) or by using the below command.
> Scale down
```
kubectl scale deployment todo-web-application --replicas=0
```
> Scale up
```
kubectl scale deployment todo-web-application --replicas=1
```
---

Using Secrets with Kubernetes
-
##### Creating Secrets
```
kubectl create secrets generic todo-web-application-secrets --from-literal=RDS_PASSWORD=dummy
```
##### Viewing Secrets
```
kubectl get secret/todo-web-application-secrets
```
##### Describe Secrets
```
kubectl describe secret/todo-web-application-secrets
```
##### Deleting Secrets
```
kubectl delete secret/todo-web-application-secrets
kubectl delete secret todo-web-application-secrets
```
- Now we have created Secrets. We can modify the deployment.yaml files to use the value from Secrets. ***(Refer deployment.yaml files for code changes)***
- After modifying deployment.yaml files, we can use `kubectl apply -f todo-web-application-deployment.yaml` to update the deployment.

Creating a ClusterIP Kubernetes Service for MySQL Database
-
- As of now we have deployed ***MySQL Database as a LoadBalancer***.
- LoadBalancer can be accessed by all. For a UI application it is fine. But a *Database need not be accessible to everyone*.
- That's why we deploy ***MySQL Database as a ClusterIP***.
- We can just modify the YAML files and update the deployment. But as of today, there're some issues while updating YAML files as ClusterIP and deploying.
- So, we can delete the MySQL service and deploy the same after modifying the YAML files.
- Command to delete a Service
```
kubectl delete service mysql
```
- After deleting the service, we modify *mysql-service.yaml* file to deploy as ClusterIP. ***(Refer mysql-service.yaml files for code changes)***
- Update the deployment
```
kubectl apply -f mysql-service.yaml
```
- To check if a ***ClusterIP*** is assigned
```
kubectl get services
```

##### Deleting the service from GCP
- Deleting by .yaml files
```
kubectl delete -f mysql-database-data-volume-persistentvolumeclaim.yaml,mysql-deployment.yaml,mysql-service.yaml,todo-web-application-deployment.yaml,todo-web-application-service.yaml
```
- Deleting by label name
```
kubectl delete all -l app=todo-web-application
```
Any of the above methods can be used to delete a service/deployment completely.