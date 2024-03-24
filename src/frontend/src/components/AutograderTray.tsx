import {Button, SimpleSelect, TextInput} from "@instructure/ui";
import React, {useEffect, useState} from "react";
import { Value } from "sass";

const AutograderTray = () => {
    const [scale, setScale] = useState(true);
    const scaleTextInput = document.getElementById("scaleTextInput");

    const handleCancel = () => {
        //CHANGE TO RETURN BACK TO ORIGINAL INPUT
    };

    const handleSave = () => {
        //CHANGE TO SAVE INPUTS
    };

    const handleAutograder = () => {
        //CHANGE TO RUN AUTOGRADER AND UPDATE STUDENT GRADES
    };

    const handleScale = () => {
        let scaleSelect = document.getElementById("scale")
        console.log(scaleSelect)
        /*if(scaleSelect == "Percent"){
            setScale(true)
        } else if (scaleSelect == "Points"){
            setScale(false)
        } else {
        }*/
    } 

    return (
        <div>
            <div className="expand-label">
                Autograder
            </div>
            <div className="specific-config-box">
                <div className="wrapper">
                <label htmlFor="present">Present: </label>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <TextInput
                    id="present"
                    className="wrapper-text"
                    onKeyDown={(event) => {
                        if(event.code.substring(0,event.code.length-1) === "Digit" || event.code.substring(0,event.code.length) === "Backspace"){
                            console.log("IS DIGIT OR BACKSPACE\n")
                        } else { 
                            event.preventDefault(); 
                        }
                    }}
                />
                </div>
                <div className="wrapper">
                <label htmlFor="late">Late: </label>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <TextInput
                    id="late"
                    className="wrapper-text"
                    onKeyDown={(event) => {
                        if(event.code.substring(0,event.code.length-1) === "Digit" || event.code.substring(0,event.code.length) === "Backspace"){
                            console.log("IS DIGIT OR BACKSPACE\n")
                        } else { event.preventDefault(); }
                    }}
                />
                </div>
                <div className="wrapper">
                <label htmlFor="absent">Absent: </label>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <TextInput
                    id="absent"
                    className="wrapper-text"
                    onKeyDown={(event) => {
                        if(event.code.substring(0,event.code.length-1) === "Digit" || event.code.substring(0,event.code.length) === "Backspace"){
                            console.log("IS DIGIT OR BACKSPACE\n")
                        } else { event.preventDefault(); }
                    }}
                />
                </div>
                <div>

                    <div className="wrapper">
                    <label htmlFor="scale">Scale: </label>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <SimpleSelect
                            width="115px"
                            id="scale"
                            onChange={() => {
                                handleScale()
                            } } 
                            renderLabel={undefined}                    >
                        <SimpleSelect.Option 
                            id="Percent"
                            value="Percent"
                        > Percent
                        </SimpleSelect.Option>
                        <SimpleSelect.Option 
                            id="Points"
                            value="Points"
                        > Points
                        </SimpleSelect.Option>
                    </SimpleSelect>
                    &nbsp;&nbsp;
                    <TextInput
                        id="scaleTextInput"
                        disabled={scale ? true : false}
                        onKeyDown={(event) => {
                            if(event.code.substring(0,event.code.length-1) === "Digit" || event.code.substring(0,event.code.length) === "Backspace"){
                                console.log("IS DIGIT OR BACKSPACE\n")
                            } else { 
                                event.preventDefault(); 
                            }
                        }}
                    />
                    </div>

                </div>
                <div className="flex pl-2">
                    <Button
                        className="autograder-btn"
                        color="primary"
                        themeOverride={{
                        mediumPaddingTop: '5px',
                        mediumPaddingBottom: '5px'
                        }}
                        onClick={handleAutograder}
                    >
                        Run Autograder
                    </Button>
                    <div className="btn-box">
                    <Button
                        className="sub-btn cancel-btn"
                        themeOverride={{
                        mediumPaddingTop: '5px',
                        mediumPaddingBottom: '5px',
                        }}
                        onClick={handleCancel}
                    >
                        Cancel
                    </Button>
                    <Button
                        className="sub-btn"
                        color="primary"
                        themeOverride={{
                        mediumPaddingTop: '5px',
                        mediumPaddingBottom: '5px'
                        }}
                        onClick={handleSave}
                    >
                        Save
                    </Button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AutograderTray;