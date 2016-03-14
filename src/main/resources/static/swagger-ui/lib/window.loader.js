   $(function () {

    addSwaggerUi("http://petstore.swagger.io/v2/swagger.json", "swagger-ui-container");
    addSwaggerUi("http://petstore.swagger.io/v2/swagger.json", "swagger-ui-container2");

    function addSwaggerUi(jsonUrl, domId) {
      window.swaggerUi = new SwaggerUi({
        url: jsonUrl,
        dom_id: domId,
        supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch'],
        docExpansion: "none",
        apisSorter: "alpha",
        showRequestHeaders: false
      });

      window.swaggerUi.load();

    }

  });