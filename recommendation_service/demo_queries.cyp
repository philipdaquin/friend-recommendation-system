
MATCH (: User {name: "John"})-[:FOLLOWS]->(:USER {name: "Peter"})

CREATE CONSTRAINT ON (user:USER) 
ASSERT (user.userId) IS UNIQUE  
ASSERT (user.friendId) IS UNIQUE 


CREATE (n:USER {
    name: "",
    userName: "",
    email: "",
    friendId: ""
}) 


MATCH (u:USER { userId: "" })
DELETE u

// CREATING a Friend Relationship between Two Users 
CREATE (u: USER {userId: {0}} )-[:FRIEND {
    createdDate: {2},
    lastModifiedDate: {3}
}]->(y: USER {1})