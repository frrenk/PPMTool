import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import setJwtToken from "../securityUtils/setJwtToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async dispatch => {
  try {
    await axios.post("/api/users/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (error) {
    dispatch({
      type: GET_ERRORS,
      payload: error.response.data
    });
  }
};

export const login = (LoginRequest, history) => async dispatch => {
  try {
    const res = await axios.post("/api/users/login", LoginRequest);
    const { token } = res.data;
    localStorage.setItem("jwtToken", token);

    setJwtToken(token);
    const decoded = jwt_decode(token);
    history.push("/dashboard");
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded
    });
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (error) {
    dispatch({
      type: GET_ERRORS,
      payload: error.response.data
    });
  }
};

export const logout = () => dispatch => {
  localStorage.removeItem("jwtToken");
  setJwtToken(false);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {}
  });
};
