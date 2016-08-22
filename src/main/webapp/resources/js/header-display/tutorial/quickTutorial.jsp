<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="tutorial-carousel">
    <slick dots="true" infinite="false" speed="100" slides-to-show="1" touch-move="false" slides-to-scroll="1"
           slick-initialized slick-slider>
        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <h1 class="only-page">
                    <em>aporlaoferta</em>
                </h1>

                <div class="mainLogo only-page"/>
                <p>¡Bienvenido!</p>

                <p>aporlaoferta es un portal para buscar y compartir ofertas nacionales con el resto de usuarios.</p>
            </div>
        </div>
        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <p>Puedes buscar las ofertas más populares del momento:</p>
                <br/>
                <ul>
                    <li>Selecciona las <strong>Últimas promociones</strong> para ver las ofertas más recientes.</li>
                    <li>Las <strong>Ofertas calientes</strong> muestran las mejores promociones de acuerdo al resto de
                        usuarios.
                    </li>
                </ul>
            </div>
        </div>
        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <p>También puedes filtrar las ofertas que más te interesen!</p>
            </div>
        </div>
        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <p>Y si encuentras una oferta que te parezca apetitosa... no dudes en compartirla!</p>
            </div>
        </div>
        <div class="tutorial-slide">
            <div class="tutorial-slide-content">
                <p>Si quieres crear ofertas y opinar sobre el resto de promociones, puedes crearte un perfil de
                    usuario</p>
                <p>¡...es totalmente gratuito!</p>
            </div>
        </div>

    </slick>
</div>
<ul class="top_header tutorial-check">
    <button class="button small icon fa-check whiteButton green-background" ng-click="hideTutorial()"
            tabindex="0">
        Entendido!
    </button>
    <button class="button mini icon fa-check whiteButton green-background account-management"
            ng-click="hideTutorial()" tabindex="0">
    </button>
</ul>
