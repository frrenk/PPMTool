import React, { Component } from "react";
import ProjectItem from "./Project/ProjectItem";
import Header from "./Layout/Header";

class Dashboard extends Component {
  render() {
    return (
      <div className="">
        <Header />
        <h1>Welcome to the dashboard</h1>
        <ProjectItem />
      </div>
    );
  }
}

export default Dashboard;
