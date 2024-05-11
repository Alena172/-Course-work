function addToCart(id) {
    const productImg = document.querySelector(`#img${id}`);
    const imgSrc = productImg.getAttribute("src");
    const productName = document.querySelector(`#title${id}`).innerText;
    let productPrice = document.querySelector(`#info${id}`).innerText;
    productPrice = parseInt(productPrice.replaceAll(' ', '').slice(0, -4));
    const inputStepper = document.querySelector(`#quantity${id}`);
    const productAmount = parseInt(inputStepper.value);

    const item = {
        id: id,
        img: imgSrc,
        name: productName,
        price: productPrice * productAmount,
        amount: productAmount,
    };

    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    const existingItemIndex = cart.findIndex(cartItem => cartItem.name === item.name);
    if (existingItemIndex !== -1) {
        cart[existingItemIndex].amount += item.amount;
        cart[existingItemIndex].price += item.price;
    } else {
        cart.push(item);
    }
    localStorage.setItem('cart', JSON.stringify(cart));
}

function removeItem(index) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    submitForm();
    fillCart();
}

function removeAll(){
    localStorage.removeItem('cart');
    localStorage.setItem('numberOfItems', 0);
    window.location.href = 'orders';
}

function decrementQuantity(id) {
    const quantityInput = document.querySelector(`#quantity${id}`);
    let quantityValue = parseInt(quantityInput.value) - 1;
    quantityValue = Math.max(quantityValue, 1);
    quantityInput.value = quantityValue;
}

function incrementQuantity(id) {
    const quantityInput = document.querySelector(`#quantity${id}`);
    let quantityValue = parseInt(quantityInput.value) + 1;
    quantityInput.value = quantityValue;
}

function fillCart() {
    let cartItemsElement = document.getElementById("cart-items");
    cartItemsElement.innerHTML = "";
    let summ = 0;
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    if (cart.length == 0){
        window.location.href = 'emptyCart';
    }
    for (let ind = 0; ind < cart.length; ind++) {
        let cartItemElement = document.createElement('div');
        cartItemElement.classList.add('oneGood');
        cartItemElement.innerHTML = `
            <div class="card">
                <img src="${cart[ind].img}" alt="${cart[ind].name}">
                <p>${cart[ind].name}</p>
            </div>
            <p>${cart[ind].amount}</p>
            <p>${cart[ind].price / cart[ind].amount} ₽</p> <!-- Цена за один товар -->
            <div class="removeAndPrice">
                <p>${cart[ind].price} ₽</p> <!-- Общая стоимость -->
                <button class="remove-btn" onclick="removeItem(${ind})"><i class="fa fa-trash" aria-hidden="true"></i></button>
            </div>`;
        let cartIndex = document.getElementById('index' + ind);
        cartIndex.value = parseInt(cart[ind].id);
        let cartName = document.getElementById('name' + ind);
        cartName.value = cart[ind].name;
        let cartAmount = document.getElementById('amount' + ind);
        cartAmount.value = parseInt(cart[ind].amount);
        let cartBookPrice = document.getElementById('price' + ind);
        cartBookPrice.value = parseInt(cart[ind].price);
        summ += cart[ind].price;
        cartItemsElement.appendChild(cartItemElement);
    }

    let totalCostElement = document.createElement('div');
    totalCostElement.classList.add('oneGood');
    totalCostElement.innerHTML = `
        <div class="card">
            <p>Общая стоимость</p>
        </div>
        <p></p>
        <p></p>
        <div class="removeAndPrice">
            <p>${summ} ₽</p>
        </div>`;
    cartItemsElement.appendChild(totalCostElement);

    let totalCostInput = document.getElementById("totalCost");
    totalCostInput.value = summ;
}

function getNumberOfItems() {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let input = document.getElementById('inputItems');
    input.value = parseInt(cart.length);
}

function submitForm() {
    getNumberOfItems();
    var form = document.getElementById("myForm");
    form.submit();
}

document.addEventListener('DOMContentLoaded', function () {
    var orderRows = document.querySelectorAll('.cart tbody tr');
    orderRows.forEach(function (row) {
        var orderId = row.querySelector('td:first-child').textContent;
        row.addEventListener('click', function () {
            loadOrderDetails(orderId);
        });
    });
});

function loadOrderDetails(orderId) {
    window.location.href = '/orderDetails?id=' + orderId;
}

