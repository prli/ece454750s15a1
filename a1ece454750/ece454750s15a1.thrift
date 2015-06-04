namespace java ece454750s15a1

struct PerfCounters {
  // number of seconds since service startup
  1: i32 numSecondsUp,
  // total number ofrequests received by service handler
  2: i32 numRequestsReceived,
  // total number ofrequests completed by service handler
  3: i32 numRequestsCompleted
}

struct ServerNode {
  1: string host,
  2: i32 pport,
  3: i32 mport,
  4: i32 ncores,
  5: optional i32 usedcores = 0
}

exception ServiceUnavailableException {
  1:string msg
}

service A1Password {
   string hashPassword (1:string password, 2:i16 logRounds) throws (1: ServiceUnavailableException e),
   bool checkPassword (1:string password, 2:string hash)
}

service A1Management {
   PerfCounters getPerfCounters(),
   list<string> getGroupMembers(),
   void addServerNode(1:string addr, 2:i32 pport, 3:i32 mport, 4:i32 ncores, 5:bool isBE),
   list<ServerNode> getAllBEServerNodes()
   list<ServerNode> getAllFEServerNodes()
}

