<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <c:if test="${not empty param.message}">
        Update successfully
    </c:if>
    <c:if test="${not empty errors}">
        There were errors while placing order
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description
                </td>
                <td>Quantity</td>
                <td class="price">Price
            </tr>
            </thead>
            <c:forEach var="item" items="${order.cartItems}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src=${item.product.imageUrl}>
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                ${item.product.description}</a></td>

                    <td>
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <p name="quantity">${item.quantity}</p>
                        <input type="hidden" name="productId" value="${item.product.id}">
                    </td>

                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/priceHistory/${item.product.id}">
                            <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td>Subtotal: ${order.subTotal}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td>Delivery: ${order.deliveryCost}</td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>Total quantity: ${cart.totalQuantity}</td>
                <td>Total price: ${order.totalCost}</td>
            </tr>
        </table>

        <br>
        <h2>Your Details</h2>
        <table>
            <tags:orderFormRow label="First name" name="firstName" errors="${errors}"/>
            <tags:orderFormRow label="Last name" name="lastName" errors="${errors}"/>
            <tags:orderFormRow label="Phone number" name="phone" errors="${errors}"/>
            <tags:orderFormRow label="Delivery date" name="deliveryDate" errors="${errors}"/>
            <tags:orderFormRow label="Delivery address" name="deliveryAddress" errors="${errors}"></tags:orderFormRow>
                <tr>
                    <td>Payment method<span style="color: red">*</span></td>
                    <td>
                        <label>
                            <select name="paymentMethod">
                                <option>${param['paymentMethod']}</option>
                                <c:forEach var="paymentMethod" items="${paymentMethods}">
                                    <c:if test="${paymentMethod != param['paymentMethod']}">
                                        <option>${paymentMethod}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </label>
                        <c:set var="error" value="${errors.get('paymentMethod')}"/>
                        <c:if test="${not empty error}">
                            <p class="error">
                                    ${error}
                            </p>
                        </c:if>
                    </td>
                </tr>
            </table>
            <button>Place order</button>
        </form>
        <form id="deleteCartItem" method="post">

        </form>
    </tags:master>
