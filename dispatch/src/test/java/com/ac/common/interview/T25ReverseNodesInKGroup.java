package com.ac.common.interview;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.ac.common.interview.T25ReverseNodesInKGroup.ListNode;

public class T25ReverseNodesInKGroup {
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode curr = null; // done
        ListNode next = null; // done
        ListNode finalListHead = null; // done
        ListNode finalListEnd = null;
        ListNode progressHead = null; // done
        ListNode progressEnd = null; // done
        ListNode forward = null;
        boolean hasEnoughNodes = true;
            

        curr = head; // done
        int count = 0; // done

        while (curr != null) {
            for (int i = 0; i < k; i++) {
                forward = curr.next;
                if (forward == null) {
                    hasEnoughNodes = false;
                    break;
                }
            }

            if (!hasEnoughNodes) {
                if (finalListHead == null) {
                    return head; // No reversal needed, return original list
                }
                finalListEnd.next = curr; // Attach the remaining nodes
                return finalListHead; // Return the modified list
            }

            next = curr.next; 
            count++; 

            if (progressEnd == null) {
                progressEnd = curr;
            }

            if (finalListEnd == null) {
                finalListEnd = curr;
            }

            if (progressHead == null) {
                curr.next = null;
                progressHead = curr;
            } else {
                curr.next = progressHead;
                progressHead = curr;
            }

            if (count == k) {
                if (finalListHead == null) {
                    finalListHead = progressHead;
                }
                finalListEnd.next = progressHead;
                finalListEnd = progressEnd;
                progressHead = null;
                progressEnd = null;
                count = 0;
            }
            curr = next;
        }

        return finalListHead;
    }

    public ListNode reverseKGroup2(ListNode head, int k) {
        ListNode curr = head;
        int count = 0;

        // Check if there are at least k nodes to reverse
        while (curr != null && count < k) {
            curr = curr.next;
            count++;
        }

        if (count < k) return head;

        // Reverse k nodes
        ListNode prev = null;
        curr = head;
        for (int i = 0; i < k; i++) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }

        // Recursively reverse remaining list and connect
        head.next = reverseKGroup(curr, k);
        return prev;
    }

    public ListNode reverseKGroup3(ListNode head, int k) {
        if (head == null || k == 1) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prevGroupEnd = dummy;
        ListNode curr = head;

        while (true) {
            // Check if there's a full k-group left
            ListNode kth = curr;
            for (int i = 1; i < k && kth != null; i++) {
                kth = kth.next;
            }

            if (kth == null) break;

            // Reverse the group
            ListNode groupStart = curr;
            ListNode nextGroupStart = kth.next;

            // Reverse the group nodes
            ListNode prev = null, node = groupStart;
            for (int i = 0; i < k; i++) {
                ListNode next = node.next;
                node.next = prev;
                prev = node;
                node = next;
            }

            // Reconnect the reversed group with the rest of the list
            prevGroupEnd.next = prev;
            groupStart.next = nextGroupStart;

            prevGroupEnd = groupStart;
            curr = nextGroupStart;
        }

        return dummy.next;
    }

    @Test
    public void test() {
        // generate a linked list for testing from 1 to 10000
        ListNode head = new ListNode(1);
        ListNode current = head;
        for (int i = 2; i <= 50000000; i++) {
            current.next = new ListNode(i);
            current = current.next;
        }
        // ListNode head = null;
        int k = 2;

        // method 2
        // 10000000 39ms
        // 50000000 564ms
        // 100000000 can't do

        Instant start = Instant.now();
        ListNode result = reverseKGroup(head, k);
        System.out.println(result.val); // Print the first node's value to verify the result
        System.out.println("Time taken: " + (Instant.now().toEpochMilli() - start.toEpochMilli()) + " ms");
        // for (ListNode node = result; node != null; node = node.next) {
        //     System.out.print(node.val + " ");
        // }
    }
}

