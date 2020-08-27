# restcarlease

A simple REST API for car leasing.
Applying example:

```
curl -X POST localhost:8080/leaseApplications -H 'Content-type:application/json'
-d '{"vehicleData": "Hyundai Ioniq", "personData": "Diane Nguyen", "personIncome":1800, "requestedAmount":20000}'
```

And later on can retrieve the application:
```
curl -v localhost:8080/leaseApplications/1
```
