
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

// Create a relationship between two users u and y 
CREATE (u: USER {userId: {0}} )-[:FRIEND {
    createdDate: {2},
    lastModifiedDate: {3}
}]->(y: USER {1})



MATCH (userA: User)-[relation:FRIEND]->(userB: User)
WHERE userA.userId={0} AND userB.userId={1}
DELETE relation


MATCH (userA:USER{userId:{0}})-[:FRIEND]-(mutuals: FRIEND)-[:FRIEND]-(userB:USER{userId: {1}})
RETURN DISTINCT mutuals



MATCH (userA: User)-[:FRIEND]-(friends), (nonFriends: User)-[:FRIEND]-(friends)
WHERE userA.userId={0}

WHERE NOT (userA)-[:FRIEND]-(nonFriends)
WITH nonFriends, count(nonFriends) as mutualFriends

RETURN nonFriends, mutualFriends
ORDER BY mutualFriends DESC



// Check if the relationship exists between the user and friend 


MATCH (userA: User), (userB: User) 
WHERE userA.userId = $userId AND userB.userId = $userId
RETURN COUNT { (userA)-[:FRIEND]->(userB) } > 1 
