import config from './config.js';
const socketBaseUrl = config.socketBaseUrl;

import { reactive } from "vue";
import { io } from "socket.io-client";

export const state = reactive({
  connected: false,
  fooEvents: [],
  barEvents: []
});

// "undefined" means the URL will be computed from the `window.location` object
// const URL = process.env.NODE_ENV === "production" ? undefined : "http://localhost:8089";
console.log({ socketBaseUrl });

export const socket = io(socketBaseUrl, {
  reconnection: false,
  transports: ['polling', 'websocket']
});

console.log('socket.io');

socket.on('connect', () => {
  console.log('connect');
  state.connected = true;
});

socket.on('disconnect', () => {
  console.log('disconnect');
  state.connected = false;
});

socket.on('foo', (...args) => {
  state.fooEvents.push(args);
});

socket.on('bar', (...args) => {
  state.barEvents.push(args);
});