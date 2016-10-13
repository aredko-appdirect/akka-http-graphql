Using GraphQL 
====

 - query all users

        curl -vi -X POST http://localhost:48080/users -H "Content-Type: application/json" -d " \
            query { \
              users { \
                id \
                email \
              } \
            }" 

 - query user by identifier

        curl -vi -X POST http://localhost:48080/users -H "Content-Type: application/json" -d " \
            query { \
              user(id: 1) { \
                email \
                firstName \
                lastName \
              } \
            }" 

 - activate user by identifier

        curl -vi -X POST http://localhost:48080/users -H "Content-Type: application/json" -d " \
           mutation {
             activateUser(id: 1) {
               activated
             }
           }"