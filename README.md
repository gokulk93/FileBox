Docker compose commands
    docker-compose -f up -d --build
    docker-compose up
    docker-compose down

Configure the AWS CLI
    aws configure

Create s3 Bucket
    aws --endpoint-url=http://localhost:4566 s3 mb s3://mytestbucket

List the buckets
    aws --endpoint-url=http://localhost:4566 s3 ls

Remove the bucket
    aws --endpoint-url=http://localhost:4566 s3 rb s3://mytestbucket

View objects inside s3 bucket
    aws --endpoint-url=http://localhost:4566 s3 ls s3://bucket-from-java2
Copy files to local
aws --endpoint-url=http://localhost:4566 s3 cp s3://bucket-from-java1/test-key-1.txt /Users/gkumar591/Downloads/files/

