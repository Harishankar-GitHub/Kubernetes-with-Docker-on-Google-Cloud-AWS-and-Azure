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
gcloud container clusters resize --zone <name_of_zone> <name_of_your_cluster> --num-nodes=0
```
##### When you are ready to start again, increase the number of nodes
```
gcloud container clusters resize --zone <name_of_zone> <name_of_your_cluster> --num-nodes=3
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
To deploy a new version: kubectl set image deployment <deployment-name> <container-name>=<new-image-name>
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
##### Steps to deploy Spring Boot Hello World Rest API to Kubernetes
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
>* Google Cloud can also be installed as a Docker Image from [Google Cloud SDK - Docker Hub](https://hub.docker.com/r/google/cloud-sdk)
>* When logging in **next** time, Open Google Cloud SDK Shell in local, type `gcloud auth login`
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
>Explanation for the above command: kubectl set image deployment <deployment-name> <container-name>=<new-image-name>

##### To view the history of deployment
```
kubectl rollout history deployment <deployment-name>
```
```
kubectl rollout history deployment hello-world-rest-api
```
>By checking the rollout history, we can see the revisions, but the CHANGE-CAUSE will be <none>.
To set the CHANGE_CAUSE, we can add ***--record*** in the command while creating/updating a deployment.
By adding --record, the **command used to update the deployment will be set to CHANGE-CAUSE**.
```
kubectl set image deployment <deployment-name> <container-name>=<new-image-name> --record
```
```
kubectl set image deployment hello-world-rest-api hello-world-rest-api=rhsb/hello-world-rest-api-kubernetes:0.0.4-SNAPSHOT --record
```
##### To check the status of deployment
```
kubectl rollout status deployment <deployment-name>
```
```
kubectl rollout status deployment hello-world-rest-api
```
##### To undo the deployment
```
kubectl rollout undo deployment <deployment-name> --to-revision=revisionNumber
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
>*This command is used to execute a specific url in specific interval. For example: Every 2 seconds.
>*To execute this command in Local, we have to install watch for our respective OS.
>*Cloud Shell that is in **Google Cloud Console UI has watch installed already**
```
watch curl URL
```
```
watch curl http://35.188.59.125:8080/hello-world
```
>We can watch logs `kubectl logs podId` and observe the logs when *watch command* continuously hits the URL. 