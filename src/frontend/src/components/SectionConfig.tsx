import {Button, TextInput, TimeSelect} from "@instructure/ui";
import axios from "axios";
import React, {useEffect, useState} from "react";
import {RiArrowDownSLine, RiArrowUpSLine} from "react-icons/ri";

import {TimeConfig, BoundError, areTimeConfigsEqual} from "./ThresholdBox";

export type CourseInfo = {
  // add more attributes moving forward
  name: string | undefined
  room: string | undefined;
  daysOfWeek: string[] | undefined;
  startTime: string | undefined;
  endTime: string | undefined;
  defaultTimeConfig: TimeConfig | undefined;
};

const SectionConfig = () => {
  const [currCourse, setCurrCourse] = useState<CourseInfo>();
  const [inputCourse, setInputCourse] = useState<CourseInfo>();
  const [error, setError] = useState<BoundError>();

  const days = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"];
  const daysLabel = ["M", "T", "W", "Th", "F"];

  const courseID = 1234;

  useEffect(() => {
    const fetchSectionConfig = async () => {
      await axios
        .get(process.env.NEXT_PUBLIC_URL + "/courseInfo/" + courseID)
        .then((res) => {
          const fetchedCourse = res.data as CourseInfo;
          console.log(fetchedCourse)
          setCurrCourse(fetchedCourse);
          setInputCourse(fetchedCourse);
        })
        .catch((err) => {
          console.error("Error fetching courseInfo:", err);
        });
    };
    fetchSectionConfig();
  }, [courseID]);


  // format time string to ISO Date string
  const formatTimeToISODate = (timeVal: string | undefined): string => {
    if (!timeVal) {
      return "";
    } else {
      const [hours, minutes] = timeVal?.split(":").map(Number);

      const date = new Date();
      date.setHours(hours, minutes, 0, 0);

      // console.log(date.toISOString());

      return date.toISOString();
    }
  };
  // retrieve time string from ISO Date string
  const retrieveTimeFromISODate = (dateVal: string | undefined): string => {
    if (!dateVal) {
      return "";
    } else {
      const formattedDate = new Date(dateVal).toLocaleString();

      const [date, time, meridian] = formattedDate.split(' ');
      let [hours, minutes] = time.split(':').map(p => parseInt(p));
      if (meridian === "PM" && hours !== 12) {
        hours += 12;
      } else if (meridian === "AM" && hours === 12) {
        hours = 0;
      }

      return hours.toString().padStart(2, '0') + ":" + minutes.toString().padStart(2, '0') + ":00";
    }
  };

  const handleStartTimeChange = (
    e: React.SyntheticEvent<Element, Event>,
    {value}: { value: string | undefined }
  ) => {
    // console.log(">>>>>>>", value);
    // Check if the selected start time is within range
    const endTime = formatTimeToISODate(inputCourse?.endTime);
    if (endTime && value && value > endTime) {
      setError({id: "startTime", isError: true, msg: "Invalid Config"});
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputCourse((prevData: CourseInfo | undefined) => {
      if (prevData) {

        return {...prevData, startTime: retrieveTimeFromISODate(value)};
      }
      return prevData;
    });
    setError({id: "startTime", isError: false, msg: "Invalid Config"});
  };

  const handleEndTimeChange = (
    e: React.SyntheticEvent<Element, Event>,
    {value}: { value: string | undefined }
  ) => {
    // console.log(">>>>>>>", value);
    // Check if the selected start time is within range
    const startTime = formatTimeToISODate(inputCourse?.startTime);

    if (startTime && value && value < startTime) {
      setError({id: "endTime", isError: true, msg: "Invalid Config"});
      return;
    }
    // the type errors are because of mock data, once pulled from api - will add the constraints
    setInputCourse((prevData: CourseInfo | undefined) => {
      if (prevData) {
        return {...prevData, endTime: retrieveTimeFromISODate(value)};
      }
      return prevData;
    });
    setError({id: "endTime", isError: false, msg: "Invalid Config"});
  };


  const handleCancel = () => {
    setInputCourse(currCourse);
  };
  const handleSave = () => {
    setCurrCourse(inputCourse);
    const updateSectionConfig = async () => {

      await axios.put(process.env.NEXT_PUBLIC_URL + "/courseInfo/" + courseID, inputCourse)
        .then((response) => {
          console.log("Section Config updated:", response.data);
        })
        .catch((error) => {
          console.error("Error updating time config:", error);
        });
    };
    updateSectionConfig();
  };

  const handleWeekDaysSelector = (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>,
    day: string) => {
    // console.log("@@@", inputCourse)
    if (Array.isArray(inputCourse?.daysOfWeek) && inputCourse?.daysOfWeek.includes(day)) {
      setInputCourse((prevData: CourseInfo | undefined) => {
        if (prevData) {
          return {...prevData, daysOfWeek: prevData?.daysOfWeek?.filter((d) => d !== day)};
        }
        return prevData;
      })
    } else if (Array.isArray(inputCourse?.daysOfWeek) && !inputCourse?.daysOfWeek.includes(day)) {
      setInputCourse((prevData: CourseInfo | undefined) => {
        if (prevData) {
          return {...prevData, daysOfWeek: prevData?.daysOfWeek?.concat(day)};
        }
        return prevData;
      })
    }
  }


  return (
    <div>
      <div className="expand-label">
        Section Config
      </div>
      <div className="specific-config-box">
        <div className="wrapper">
          <label htmlFor="sectionName">Section Name: </label>
          &nbsp;&nbsp;
          <select id="sectionName">
            <option value="sectionA">Section A</option>
            <option value="sectionB">Section B</option>
          </select>
        </div>
        <div className="wrapper">
          <label htmlFor="location">Location: </label>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <TextInput
            id="location"
            className="wrapper-text"
            onChange={(event, value) => {
              setInputCourse((prevData: CourseInfo | undefined) => {
                if (prevData) {
                  return {...prevData, room: value};
                }
                return prevData;
              });
            }}
            value={inputCourse?.room}
          />
        </div>
        <div className="wrapper">
          <label htmlFor="weekdays">Weekdays: </label>
          &nbsp;&nbsp;&nbsp;
          <div id="weekdays" className="week-days-container">
            {days.map((day, index) => (
              <button
                key={day}
                className={`day-circle ${
                  Array.isArray(inputCourse?.daysOfWeek) && inputCourse?.daysOfWeek.includes(day) ? "highlight" : ""
                }`}
                onClick={(e) => handleWeekDaysSelector(e, day)}
              >
                {daysLabel[index]}
              </button>
            ))}
          </div>
        </div>
        <div className="wrapper">
          <label htmlFor="startTime">Start Time: </label>
          &nbsp;&nbsp;
          <TimeSelect
            renderLabel=""
            id="startTime"
            step={5}
            onChange={(e, {value}) => handleStartTimeChange(e, {value})}
            onInputChange={(e, value, isoValue) =>
              setInputCourse((prevData: CourseInfo | undefined) => {
                if (prevData) {
                  return {...prevData, startTime: retrieveTimeFromISODate(isoValue)};
                }
                return prevData;
              })
            }
            value={formatTimeToISODate(inputCourse?.startTime)}
            allowNonStepInput
            width="19rem"
          />
        </div>
        <div className="wrapper">
          <label htmlFor="endTime">End Time: </label>
          &nbsp;&nbsp;&nbsp;
          <TimeSelect
            renderLabel=""
            id="endTime"
            step={5}
            onChange={(e, {value}) => handleEndTimeChange(e, {value})}
            onInputChange={(e, value, isoValue) =>
              setInputCourse((prevData: CourseInfo | undefined) => {
                if (prevData) {
                  return {...prevData, endTime: retrieveTimeFromISODate(isoValue)};
                }
                return prevData;
              })
            }
            value={formatTimeToISODate(inputCourse?.endTime)}
            allowNonStepInput
            width="19rem"
          />
        </div>
        <div className="btn-box">
          <Button
            className="sub-btn cancel-btn"
            themeOverride={{
              mediumPaddingTop: '5px',
              mediumPaddingBottom: '5px',
            }}
            onClick={handleCancel}
            disabled={inputCourse === currCourse}
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
            disabled={inputCourse === currCourse}
          >
            Save
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SectionConfig;
