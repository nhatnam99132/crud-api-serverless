aws cloudformation validate-template --template-body file://template.yml
sam build
sam local start-api
sam deploy --template-file template.yml --stack-name sam-app --capabilities CAPABILITY_IAM
aws cloudformation delete-stack --stack-name sam-app

aws cloudformation describe-change-set --change-set-name samcli-deploy1735459207 --stack-name sam-app

