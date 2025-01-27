<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
       ${product.description}
    </p>
    <p> ${cart} </p>

    <c:if test="${not empty param.message}">
        <div class="success">
            ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">
                ${error}
        </p>
    </c:if>

    <form method="post">
    <table>

        <tr>
            <td>Image</td>
            <td>
                <img class="product-tile" src=${product.imageUrl}>
            </td>
        </tr>
        <tr>
            <td>Code</td>
            <td>
                    ${product.code}
            </td>
        </tr>

        <tr>
            <td>Stock</td>
            <td>
                    ${product.stock}
            </td>
        </tr>

        <tr>
            <td>Price</td>
            <td class="price">
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>


        <tr>
            <td>Quantity</td>
            <td class="price">
                <input name="quantity" value="${not empty param.quantity ? param.quantity : 1}">
            </td>

        </tr>
    </table>
        <button>Add to cart</button>
    </form>
</tags:master>
