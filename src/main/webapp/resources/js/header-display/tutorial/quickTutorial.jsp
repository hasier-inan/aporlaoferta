<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="tutorial-carousel">
    <slick dots="true" infinite="false" speed="100" slides-to-show="1" touch-move="false" slides-to-scroll="1"
           slick-initialized slick-slider>
        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <h1 class="only-page">
                    <em class="main-logo__brand">aporlaoferta</em>
                </h1>

                <div class="social-media-shares">
                    <a class="icon mini hvr-icon-grow fa-facebook social-share" target="_blank"
                       href="https://www.facebook.com/aporlaoferta">
                    </a>
                    <a class="icon mini hvr-icon-grow fa-twitter social-share" target="_blank"
                       href="https://twitter.com/aporlaoferta">
                    </a>
                    <a class="icon mini hvr-icon-grow fa-google-plus social-share" target="_blank"
                       href="https://plus.google.com/112814296578233509279">
                    </a>
                </div>
                <br/>

                <p>¡Bienvenido!</p>

                <p>
                    aporlaoferta.com es un portal donde compartir y conseguir las mejores ofertas en todo lo que te
                    puedas imaginar.
                </p>
            </div>
        </div>

        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <br/>
                <p><i class="tutorial-slide-content__icon fa fa-refresh loadingdialogue"></i></p>
                <ul>
                    <li class="tutorial-slide-content__referenced-text">Con un solo click en <a href="#"
                                                                                                class="button medium--rounded hvr-icon-grow newest-offers icon fa-shopping-cart whiteButton scrolly main-selection"
                                                                                                ng-click="requestNewestOffers()">
                        Últimas promociones</a> podrás ser el primero en conseguir las ofertas más recientes.
                    </li>
                </ul>
                <br/>
                <p class="offer-temperature">
                    <i class="tutorial-slide-content__icon offer-thermometer offer-thermometer--big veryHotFeedback"></i>
                </p>
                <ul>
                    <li class="tutorial-slide-content__referenced-text">
                        Elige las <a href="#"
                                     class="button medium--rounded hvr-icon-pulse-grow hot-offers icon fa-fire whiteButton scrolly main-selection"
                                     ng-click="requestHottestOffers()">
                        Ofertas calientes</a> para conseguir los chollos más populares del momento.
                    </li>
                </ul>
            </div>
        </div>

        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <br/>
                <p>Selecciona tu <strong>Categoría</strong> preferida o utiliza la casilla de <strong>Buscar</strong>
                    para encontrar las ofertas que más te interesan:</p>

                <div id="contentFilterTutorial" class="wrapper style2 special" ng-offer-filter="offerFilter"
                     offer-list="offerList" selection="defaultList" offer-filter="offerFilter"></div>
                <p>¡Hay de todo y para todos!</p>
            </div>
        </div>

        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <p class="no-margin">
                    <i class="tutorial-slide-content__icon main-logo"></i>
                </p>
                <ul>
                    <li>¡Nunca te pierdas una oferta!</li>
                    <li>Accede a todas las promociones y compártelas con todos tus amigos sin necesidad de
                        registrarte.
                    </li>
                </ul>
                <br/>
                <p class="no-margin">
                    <i class="tutorial-slide-content__icon tutorial-slide-content__icon--margin-right tutorial-slide-content__icon--blue fa fa-user"></i>
                    <i class="tutorial-slide-content__icon tutorial-slide-content__icon--blue fa fa-sign-in"></i></p>
                <p>…Pero no dudes en unirte a nosotros si quieres publicar, comentar y votar las ofertas. ¡Vive la
                    experiencia al máximo!</p>
                <p>¡Registrarse es rápido y totalmente gratuito!</p>
            </div>
        </div>

    </slick>
</div>
