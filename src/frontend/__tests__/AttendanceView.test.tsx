import '@testing-library/jest-dom';
import React from 'react';
import {act, fireEvent, render, screen, within} from '@testing-library/react';
import AttendanceView from '@/components/AttendanceView';
import userEvent from '@testing-library/user-event';

// Only 17th july has the required mock data but that might change
const fireDateChangeEvent = async (dateValue: string) => {
   const user = userEvent.setup();
   const dateInput = screen.getByRole('combobox', {name: 'section date selector'})

   await userEvent.clear(dateInput);
   await user.click(dateInput);
   await user.keyboard('07/17/2023');
};

// Firstly we are checking if we are rendering correctly
test('should render attendance data records', async () => {
  const user = userEvent.setup();
  const result = render(<AttendanceView />);
  
  const dateInput = screen.getByRole('combobox', {name: 'section date selector'})

  await userEvent.clear(dateInput);
  await user.click(dateInput);
  await user.keyboard('07/17/2023');

  let attendance_table = screen.getAllByRole('row')

  expect(attendance_table).toBeInTheDocument;
})

test('should set 17th july 2023 as date', async () => {
  render(<AttendanceView />);
  await fireDateChangeEvent('07/17/2023');

  let date = screen.getByLabelText('section date selector')

  expect(date.getAttribute('value')).toBe('07/17/2023')
});

test('should render AttendanceView with header "Attendance Table View"', () => {
  render(<AttendanceView />);

  const headerElement = screen.getByText('Attendance Table View');

  expect(headerElement).toBeInTheDocument();
});

test('should render filtering toggle group', () => {
  render(<AttendanceView />);

  const filteringLabel = screen.getByText('Filtering');

  expect(filteringLabel).toBeInTheDocument();
})

test('should render "No attendance records found" if no data exists', () => {
  render(<AttendanceView />);

  const noRecordsFoundLabel = screen.getByText('No attendance records found.');

  expect(noRecordsFoundLabel).toBeInTheDocument();
});

test.only('should check if sorting by time works', async () => {
  const user = userEvent.setup();
  const { container } = render(<AttendanceView />);
  await fireDateChangeEvent('07/17/2023');

  const timeColumnHeader = container.querySelector("css-1386l9d-colHeader__button");
  user.click(timeColumnHeader);

  expect(timeColumnHeader).toHaveAttribute('aria-sort', 'descending');
});