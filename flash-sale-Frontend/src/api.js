import axios from 'axios';

export const inventoryApi = axios.create({
  baseURL: 'http://localhost:8081/api/inventory',
});

export const orderApi = axios.create({
  baseURL: 'http://localhost:8080/api/orders',
});
