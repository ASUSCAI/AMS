import React, { useRef, useState } from "react";
//import { useRouter } from 'next/router';
import { SplitButton } from "primereact/splitbutton";
import { Toast } from "primereact/toast";
import "primereact/resources/themes/saga-green/theme.css";

export default function PairSplitButton() {
  //const router = useRouter();
  const toast = useRef(null);
  const [paired, setPaired] = useState<string>("None");
  const [defaultReader, setDefaultReader] = useState<string>("None");

  // fetch default reader for this section

  const items = [
    {
      label: "Reader 1",
      className: "pair-item",
      // icon: 'pi pi-refresh',
      command: () => {
        // send request to connect to reader
        // set to defaultReader as well
        // setPaired
        setDefaultReader("Reader 1");
        setPaired("Reader 1");
        (toast.current as any)?.show({
          severity: "success",
          summary: "Success",
          detail: "Reader 1 Paired - Reader 1 is now Default Reader",
          className: "toast-cont",
        });
      },
    },
    {
      label: "Reader 2",
      className: "pair-item",
      // icon: 'pi pi-times',
      command: () => {
        // send request to connect to reader
        // set to defaultReader as well
        // setPaired
        setDefaultReader("Reader 2");
        setPaired("Reader 2");
        (toast.current as any)?.show({
          severity: "success",
          summary: "Success",
          detail: "Reader 2 Paired - Reader 2 is now Default Reader",
          className: "toast-cont",
          
        });
      },
    },
    {
      label: "Reader 3",
      className: "pair-item",
      command: () => {
        // send request to connect to reader
        // set to defaultReader as well
        // setPaired
        setDefaultReader("Reader 3");
        setPaired("Reader 3");
        (toast.current as any)?.show({
          severity: "success",
          summary: "Success",
          detail: "Reader 3 Paired -  Reader 3 is now Default Reader",
          className: "toast-cont",
          
        });
      },
    },
    {
      label: "Reader 4",
      className: "pair-item",
      // icon: 'pi pi-upload',
      command: () => {
        // send request to connect to reader
        // set to defaultReader as well
        // setPaired
        setDefaultReader("Reader 4");
        setPaired("Reader 4");
        (toast.current as any)?.show({
          severity: "success",
          summary: "Success",
          detail: "Reader 4 Paired - Reader 4 is now Default Reader",
          className: "toast-cont",
          
        });
      },
    },
  ];

  const [loading, setLoading] = useState(false);

  const handlePair = () => {
    // check if defaultReader is null or not
    if (paired !== "None") {
      setPaired("None");
      // send request to disconnet reader
    } else {
      if (defaultReader !== "None") {
        setPaired(defaultReader);
        // send request to connect reader
        (toast.current as any)?.show({
          severity: "success",
          summary: "Success",
          detail: defaultReader + " Paired",
          className: "toast-cont",
        });
      } else {
        (toast.current as any)?.show({
          severity: "warn",
          summary: "No Default Reader",
          detail: "Select a Reader",
          className: "toast-cont toast-pair-warn",
          styleClass: "toast-icon",
        });
      }
    }
  };

  return (
    <div>
      <Toast ref={toast}></Toast>
      <SplitButton
        className={`pair-btn ${paired === "None" ? '' : 'paired'}`}
        menuButtonClassName={`pair-menu-btn ${paired === "None" ? '' : 'paired-menu'}`}
        menuClassName="pair-menu-dropdown"
        label={paired === "None" ? "Pair" : "Paired"}
        icon="pi"
        onClick={handlePair}
        model={items}
        loading={loading}
      />
    </div>
  );
}
