<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty errors}">
    There were errors while adding product to your cart
  </c:if>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description
       <tags:sortLink sort="description" order="asc"></tags:sortLink>
       <tags:sortLink sort="description" order="desc"></tags:sortLink>
        </td>
        <td>Quantity</td>
        <td class="price">Price
            <tags:sortLink sort="price" order="asc"></tags:sortLink>
            <tags:sortLink sort="price" order="desc"></tags:sortLink>
        </td>
        <td>

        </td>
      </tr>

    </thead>
    <c:forEach var="product" items="${products}" varStatus="status">
      <tr>
        <td>
          <img class="product-tile" src=${product.imageUrl}>
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}</a></td>

        <td>
          <fmt:formatNumber value="1" var="quantity"/>
          <input form="addItemToCart/${product.id}" name="quantity"
                 value="${products.get(status.index).id == param.productId
                           ? (not empty param.quantity ? param.quantity : 1)
                           : 1}"/>
          <c:if test="${not empty param.error && products.get(status.index).id == param.productId}">
            <div class="error">
                ${param.error}
            </div>
          </c:if>
          <input form="addItemToCart/${product.id}"
                 type="hidden"
                 name="productId"
                 value="${product.id}"/>
        </td>


        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/priceHistory/${product.id}">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
        </td>

        <td>
          <button form="addItemToCart/${product.id}"
                  formaction="${pageContext.servletContext.contextPath}/cart/addItemToCart/${product.id}">Add to Cart</button>
        </td>
      </tr>
      <form id="addItemToCart/${product.id}" method="post"></form>
    </c:forEach>
  </table>

  <c:if test="${not empty viewedProducts}">
    <div class="viewed-items" style="display: flex; flex-direction: row">
      <c:forEach var="product" items="${viewedProducts}">
        <div class="viewed-item" style="padding: 40px 50px">
          <img class="product-tile" src="${product.imageUrl}">
          <p><a href="${pageContext.request.contextPath}/products/${product.id}">${product.description}</a></p>
        </div>
      </c:forEach>
    </div>
  </c:if>
</tags:master>