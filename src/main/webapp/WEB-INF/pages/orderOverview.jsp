<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
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
            <tags:overviewRow label="First name" name="firstName" order="${order}"/>
            <tags:overviewRow label="Last name" name="lastName"  order="${order}"/>
            <tags:overviewRow label="Phone number" name="phone"  order="${order}"/>
            <tags:overviewRow label="Delivery date" name="deliveryDate" order="${order}"/>
            <tags:overviewRow label="Delivery address" name="deliveryAddress"  order="${order}"/>
                <tr>
                    <td>Payment method<span style="color: red">*</span></td>
                    <td>
                        <label>
                            ${order.paymentMethod}
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
    </tags:master>
