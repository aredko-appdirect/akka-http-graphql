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
           mutation { \
             activateUser(id: 1) { \
               id \
               activated \
               locale  \
               company { \
                 name \
               } \
               address { \
                 street \
                 country \
               } \
             } \
           }"

 - add user

        curl -vi -X POST http://localhost:48080/users -H "Content-Type: application/json" -d " \
           mutation { \
             addUser(email: \"a@b.com\", firstName: \"John\", lastName: \"Smith\", roles: [CHANNEL_ADMIN]) { \
               id \
               activated \
               email \
               firstName \
               lastName \
               locale  \
               company { \
                 name \
               } \
               address { \
                 street \
                 country \
               } \
             } \
           }"