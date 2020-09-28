# myweather-api
API Rest de cadastro de cidades e consulta de previsão do tempo

### Configurando ambiente

Este projeto foi configurado para uso do banco de dados *MySQL*.
Atualize o arquivo _application.properties_ conforme o seu SGBD/credenciais, bem como os scripts de migração do framework FlyWay, localizados em _/src/main/resources/db/migration_

A _myweather-api_ utiliza a API [OpenWeather](https://openweathermap.org/api) para consultar a previsão do tempo. É necessário criar uma conta gratuita e informar a chave de autenticação no arquivo _applicaton.properties_

A _myweather-api_ utiliza a API [OwnCityFinder](https://gitlab.com/mvysny/owm-city-finder/tree/master/owm-city-finder-server) para garantir que as cidades cadastradas possuem correspondência na API OpenWeather.
É necessário rodar a imagem Docker em conjunto com esta API para obter o correto funcionamento, conforme descrito na página do GitHub

### Instruções de uso

A documentação pode ser obtida através do _endpoint_ */swagger-ui.html*, acionado com o verbo _GET_ no browser de sua preferência


