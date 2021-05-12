# applause-url-shortner
This is a demo project to implement url shorter using Rest Api

# Add shorten url from original url 
Method: POST
API: http://localhost:8080/api/url

Json data:
{
	"urlValue":"http://google111.com"
}

# Get Url from shorten url
Method: PUT
API: http://localhost:8080/api/url

Json data:
{
	"urlValue":"https://applau.se/Z9"
}


# Get All Url
Method: GET
API: http://localhost:8080/api/url

