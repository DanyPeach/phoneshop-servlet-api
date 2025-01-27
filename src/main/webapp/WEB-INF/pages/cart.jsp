<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
       Cart total quantity: ${cart.totalQuantity} | Cart total cost: ${cart.totalCost}
    </p>

    <c:if test="${not empty param.message}">
        Update successfully
    </c:if>
    <c:if test="${not empty errors}">
        There were errors while updating cart
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description
            </td>
            <td>Quantity</td>
            <td class="price">Price
            <td>Delete</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${cart.cartItems}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile" src=${item.product.imageUrl}>
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                            ${item.product.description}</a></td>

                <td>
                    <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                    <c:set var="error" value="${errors[item.product.id]}"/>
                    <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"/>
                    <c:if test="${not empty error}">
                        ${errors[item.product.id]}
                    </c:if>
                    <input type="hidden" name="productId" value="${item.product.id}">
                </td>

                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/priceHistory/${item.product.id}">
                        <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                    </a>
                </td>

                <td>
                    <button form="deleteCartItem"
                            formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
                </td>
            </tr>
        </c:forEach>
    </table>
        <c:if test="${cart.cartItems.size()!=0}">
        <button>Update</button>
        <br>
        </c:if>
      </form>
    <c:if test="${cart.cartItems.size()!=0}">
    <form action="${pageContext.servletContext.contextPath}/checkout">
        <button>Checkout</button>
    </form>
    </c:if>
    <form id="deleteCartItem" method="post">

    </form>
</tags:master>
