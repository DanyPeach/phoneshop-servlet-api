<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="AdvancedSearch">
  <p>
    Welcome to Expert-Soft training!
  </p>


  <form method="get" action="${pageContext.servletContext.contextPath}/advancedSearch">
    <label>
      Description: <input name="query" value="${param.query}">
      <select name="searchOption">
        <option name="ALL_WORDS">ALL_WORDS</option>
        <option name="ANY_WORDS">ANY_WORDS</option>
      </select>
    </label>
    <br>
    <br>
    <label>
      Min price: <input type="text" name="minPrice"
                        value="${param.minPrice}">
    </label>
    <div class="error" style="color: red">${requestScope.errors.get("error")}</div>
    <br>
    <label>
      Max price: <input type="text" name="maxPrice"
                        value="${param.maxPrice}">
    </label>
    <div class="error" style="color: red">${requestScope.errors.get("error")}</div>
    <br>
    <br>
    <button>Search</button>
  </form>

  <div class="success">
    Found ${products.size()} products
  </div>


  <c:if test="${not empty requestScope.products and empty requestScope.errors}">
  <table>
    <thead>
    <tr>
      <td>Image</td>
      <td class="description">
        Description
        <tags:sortLink sort="description" order="asc"/>
        <tags:sortLink sort="description" order="desc"/>
      </td>
      <td class="price">
        Price
        <tags:sortLink sort="price" order="asc"/>
        <tags:sortLink sort="price" order="desc"/>
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
              ${product.description}
          </a>
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}/priceHistory">
            <fmt:formatNumber value="${product.price}" type="currency"
                              currencySymbol="${product.currency.symbol}"/>
          </a>
        </td>
      </tr>
    </c:forEach>
  </table>
  </c:if>
</tags:master>
</form>