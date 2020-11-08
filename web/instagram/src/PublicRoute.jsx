import React from "react";
import { Redirect, Route } from "react-router-dom";

const PublicRoute = ({ path, component }) => {
  const isAuthenticated = !!localStorage.getItem("token");

  if (isAuthenticated) return <Redirect to={"/profile"} />;

  return <Route path={path} component={component} />;
};

export default PublicRoute;