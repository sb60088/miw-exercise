MIW-Exercise

1. This MVC module is created using spring boot, security, rest, h2 db , mvn, git and java sdk
2. Quickly used spring-initialzr to create a mvn project. Added all relevant dependencies
3. Initialised git repo and cloned it. Added relevant code
4. Surge Pricing
   3.1 I create 2 tables i.e ITEMS( to store item information) and VISITORS ( to store unique visitor based on ip addr)
   3.2 For any item lookup, i select data from both tables using row_number ( partitioned by item_id order by visit_date) where rn<11. 
        This ensures only last 10 records were retrieved per itemId.
        Then In java, i get the first element(out of 10 records) and compared it's visit time(when this product was viewed for ip addr) within 1 hour range from new Date() [now]
        If yes, i increase the price by 10% and show to the UI else not increase the price
5. I assume - if price is surged let's say at 11 am, and no views was there for that product in the next one hour, then price is reset to original rate
6. I assume - if price is surged let's say at 11 am, and more views was there for that product in the next one hour, then price is increased by 10  to original rate ( not to the already increased rate)

Data Format
1. For response, I used JSON
   Input Request for product lookup
   {
   "itemId": "item_3",
   "ipAddr": "12.12.13"
   }

  Response:
  [
  {
  "itemId": "item_3",
  "itemDesc": "monitor",
  "itemPrice": 400.23,
  "itemCount": 10
  },
  {
  "itemId": "item_2",
  "itemDesc": "keyboard",
  "itemPrice": 50,
  "itemCount": 10
  },
  {
  "itemId": "item_1",
  "itemDesc": "mouse",
  "itemPrice": 20,
  "itemCount": 10
  }
  ]
  
Authentication
1. For endpoints like /listInventory or /listByItem, i have excluded authentication as guests users can also view the inventory
2. for buy endpoint, user needs to have role has registered user and this is only possible when user log in from UI entering credentials
    2.1 JwtAuthenticationController is free from spring security and does 2 things
        2.1.1 authenticates user
            2.1.1.1 For Authentication, I am returning true always but can be changed as per LDAP authentication or OAUTH authentication. I don;t have any server created therefore, added dummy implementation
        2.1.2 If success then generates JWT token with claim as REGISTERED user
    2.2 JwtAuthorizationFilter checks the Auth header, verify JWT token for registered user. If true, then request is further processed else 401 Un-Authorized error is returned.

This authentication/authorization scheme is useful as UI can maintain token at it end till it's expiry and refresh the token and keep the work going