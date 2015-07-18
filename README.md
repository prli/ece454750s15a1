# ece454750s15a1

In this assignment you will build a scalable distributed system for computing the bcryptkey derivation function, which is a popular technique for securing passwords in web applications.The system will comprise a front end (FE) layer and a back end (BE) layer.  The FE layer will accept connections from clients and forward requests to the BE layer in a manner that balances load.  Both layers will be distributed horizontally for scalability.Furthermore, the system will support elastic scalability and rudimentary fault tolerance.


Learning objectives

To gain hands-on experience with Apache Thrift:

    •defining and implementing RPC interfaces
    
    •selecting Thrift protocols and server implementations
  
To develop a basic understanding of scalability:

    •request level parallelism (RLP)
  
    •load balancingTo develop a basic understanding of fault tolerance:
  
    •detecting failures
  
    •maintaining service availability despite server crashes
  
