aws cloudformation validate-template --template-body file://template.yml
sam build --template template_local.yml
sam build
sam local start-api
aws dynamodb create-table --table-name orders --attribute-definitions AttributeName=id,AttributeType=S --key-schema AttributeName=id,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

sam deploy --template-file template.yml --stack-name sam-app --capabilities CAPABILITY_IAM
aws cloudformation describe-change-set --change-set-name samcli-deploy1735459207 --stack-name sam-app

aws cloudformation delete-stack --stack-name sam-app
aws dynamodb delete-table --table-name orders
